package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.ListEnvelope;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class WebCrawlerService extends Service {
    private static final String TAG = "WebCrawlerService";
    private final Object LOCK = new Object();

    private ProfileClient _profileClient;
    private WorkordersWebApi _workorderClient;
    private BundlesWebApi _bundleClient;
    private final ThreadManager _workorderThreadManager = new ThreadManager();
    private final List<WorkOrder> _workorderDetails = new LinkedList<>();

    private Handler _activityHandler;

    private boolean _haveProfile = false;
    private long _lastRequestTime;
    private long _requestCounter = 0;
    private boolean _isRunning = false;
    private long _imageDaysToLive = -1;
    private boolean _runningPurge = false;
    private boolean _monitorRunning = false;

    public WebCrawlerService() {
        super();
        Log.v(TAG, "WebCrawlerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

        _imageDaysToLive = Integer.parseInt(settings.getString(getString(R.string.pref_key_remove_rate), "-1")) * 2;

        purgeOldData();

        // we're not allowed to run, stop
        if (!settings.getBoolean(getString(R.string.pref_key_sync_enabled), false)) {
            Log.v(TAG, "sync disabled, quiting");
            startActivityMonitor();
            return START_NOT_STICKY;
        }

        scheduleNext();

        // if already running, then return
        if (_isRunning) {
            Log.v(TAG, "already running, stopping");
            return START_STICKY;
        }

        // if not running then
        if (intent != null && intent.hasExtra("IS_ALARM")) {
            Log.v(TAG, "alarm triggered");

            runCrawler();

            return START_STICKY;
        }

        Log.v(TAG, "Do nothing");
        startActivityMonitor();
        return START_STICKY;
    }

    private void purgeOldData() {
        if (_runningPurge)
            return;

        _runningPurge = true;
        // TODO do the purge... cause we got nowhere else to do it right now
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Log.v(TAG, "Flushing logs");
                DebugUtils.flushLogs(WebCrawlerService.this, 86400000); // 1 day
                Log.v(TAG, "flushing data");
                StoredObject.flush(App.get(), 604800000); // 1 week
                //StoredObject.flush(1000); // 1 week

                Log.v(TAG, "_imageDaysToLive: " + _imageDaysToLive + " haveWifi: " + App.get().haveWifi());
                // only flush if we have wifi, so that the app can get new ones without
                // worrying about cell traffic
                if (_imageDaysToLive > -1 && App.get().haveWifi()) {
                    long days = _imageDaysToLive * 86400000;
                    long cutoff = System.currentTimeMillis();
                    List<StoredObject> list = StoredObject.list(App.get(), App.getProfileId(), "PhotoCache");

                    Log.v(TAG, "Flushing photos");
                    int count = 0;
                    for (StoredObject obj : list) {
                        if (obj.getLastUpdated() + days < cutoff) {
                            StoredObject.delete(App.get(), obj);
                            count++;
                        }
                    }

                    list = StoredObject.list(App.get(), App.getProfileId(), "PhotoCacheCircle");

                    for (StoredObject obj : list) {
                        if (obj.getLastUpdated() + days < cutoff) {
                            StoredObject.delete(App.get(), obj);
                            count++;
                        }
                    }

                    Log.v(TAG, "Flushing photos " + count);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                _runningPurge = false;
            }
        }.executeEx();
    }

    private synchronized void incrementPendingRequestCounter(int val) {
        Log.v(TAG, "_workorderDetails.size() = " + _workorderDetails.size());
    }

    private synchronized void incRequestCounter(int val) {
        _lastRequestTime = System.currentTimeMillis();
        _requestCounter += val;
        if (_requestCounter % 5 == 0)
            Log.v(TAG, "_requestCounter = " + _requestCounter);
    }

    private void startActivityMonitor() {
        if (_activityHandler == null)
            _activityHandler = new Handler();

        if (!_monitorRunning) {
            _monitorRunning = true;
            _activityHandler.postDelayed(_activityMonitor_runnable, 60000);
        }
    }

    private final Runnable _activityMonitor_runnable = new Runnable() {
        @Override
        public void run() {
            _monitorRunning = false;// check timer
            if (System.currentTimeMillis() - _lastRequestTime > 60000
                    && !_runningPurge) {

                // shutdown
                stopSelf();
            } else {
                startActivityMonitor();
            }
        }
    };

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (_workorderClient != null)
            _workorderClient.disconnect(this);
        if (_profileClient != null)
            _profileClient.disconnect(this);
        if (_workorderThreadManager != null)
            _workorderThreadManager.shutdown();
        if (_bundleClient != null)
            _bundleClient.disconnect(this);

        _isRunning = false;
        super.onDestroy();
    }

    private void scheduleNext() {
        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

        // if clock is not set, set it
        long runTime = settings.getLong(getString(R.string.pref_key_sync_start_time), 180);

        Calendar cal = Calendar.getInstance();
        if (BuildConfig.DEBUG) {
            cal.set(Calendar.HOUR_OF_DAY, (int) (runTime / 60));
            cal.set(Calendar.MINUTE, (int) (runTime % 60));
        } else {
            Random random = new Random();
            cal.set(Calendar.HOUR_OF_DAY, (int) (runTime / 60) + random.nextInt(1));
            cal.set(Calendar.MINUTE, (int) (runTime % 60) + random.nextInt(60));
        }

        long nextTime = cal.getTimeInMillis();
        if (nextTime < System.currentTimeMillis()) {
            nextTime += 86400000;
        }

        AlarmBroadcastReceiver.registerCrawlerAlarm(this, nextTime);
        Log.v(TAG, "register sync alarm " + ISO8601.fromUTC(nextTime));
    }

    public void runCrawler() {
        Log.v(TAG, "runCrawler");

        if (_isRunning) {
            Log.v(TAG, "crawler skipping");
            return;
        }

        _lastRequestTime = System.currentTimeMillis();
        _isRunning = true;

        startActivityMonitor();

        _workorderThreadManager.addThread(new WorkorderDetailWorker(_workorderThreadManager, this, _workorderDetails));

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(this);

        _workorderClient = new WorkordersWebApi(_workorderClient_listener);
        _workorderClient.connect(this);

        _bundleClient = new BundlesWebApi(_bundlesClient_listener);
        _bundleClient.connect(this);
    }

    private final WorkordersWebApi.Listener _workorderClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected");
            _workorderClient.subWorkordersWebApi();

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            WorkordersWebApi.getWorkOrderLists(WebCrawlerService.this, false, true);
        }

        @Override
        public void onGetWorkOrderLists(SavedList[] savedList, boolean success, Error error) {

            incrementPendingRequestCounter(-1);

            for (SavedList list : savedList) {
                incrementPendingRequestCounter(1);
                incRequestCounter(1);
                WorkordersWebApi.getWorkOrders(WebCrawlerService.this, new GetWorkOrdersOptions().list(list.getId()).page(1), false, true);
            }
        }

        @Override
        public void onGetWorkOrders(WorkOrders workOrders, boolean success, Error error) {
            incrementPendingRequestCounter(-1);

            // get the details
            WorkOrder[] works = workOrders.getResults();
            for (WorkOrder workOrder : works) {
                incrementPendingRequestCounter(1);
                incRequestCounter(1);
                WorkordersWebApi.getWorkOrder(WebCrawlerService.this, workOrder.getId(), false, true);
            }

            //
            ListEnvelope metadata = workOrders.getMetadata();
            if (metadata.getPage() == 1) {
                for (int i = 2; i <= workOrders.getMetadata().getPage() + 1; i++) {
                    incrementPendingRequestCounter(1);
                    incRequestCounter(1);
                    WorkordersWebApi.getWorkOrders(WebCrawlerService.this, new GetWorkOrdersOptions().list(workOrders.getMetadata().getList()).page(i), false, true);
                }
            }
        }

        @Override
        public void onGetWorkOrder(WorkOrder workOrder, boolean success, Error error) {
            incrementPendingRequestCounter(-1);

            if (workOrder.getBundle() != null && workOrder.getBundle().getId() != null && workOrder.getBundle().getId() > 0) {
                incrementPendingRequestCounter(1);
                incRequestCounter(1);
                BundlesWebApi.getBundleWorkOrders(WebCrawlerService.this, workOrder.getBundle().getId(), false, true);
            }

            _workorderDetails.add(workOrder);
            _workorderThreadManager.wakeUp();

            // TODO get messages for work order
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_profileClient_listener.onConnected");
            _profileClient.subListMessages(true);
            _profileClient.subListNotifications(true);
            _profileClient.subGet(true);

            incrementPendingRequestCounter(3);
            incRequestCounter(3);
            _haveProfile = false;
            ProfileClient.get(WebCrawlerService.this, 0, true, false);
            ProfileClient.listMessages(WebCrawlerService.this, 0, true, false); // TODO this is not returning sometimes
            ProfileClient.listNotifications(WebCrawlerService.this, 0, true, false); // TODO this is not returning sometimes
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            Log.v(TAG, "ProfileClient.onGet " + _haveProfile);
            if (!_haveProfile) {

                incrementPendingRequestCounter(-1);

                if (failed)
                    return;

                Log.v(TAG, "ProfileClient.onGet");
                incRequestCounter(2);
                PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getLarge(), true, true);
                PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getThumb(), true, true);
                _haveProfile = true;
            }
        }

        @Override
        public void onMessageList(List<Message> list, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "ProfileClient.onMessageList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0 || failed) {
                _workorderThreadManager.wakeUp();
                return;
            }
            Log.v(TAG, "onMessageList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listMessages(WebCrawlerService.this, page + 1, true, false);

            for (int i = 0; i < list.size(); i++) {
                Message message = list.get(i);
                if (message.getFromUser() != null) {
                    if (!misc.isEmptyOrNull(message.getFromUser().getPhotoUrl())) {
                        incRequestCounter(1);
                        PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    } else if (!misc.isEmptyOrNull(message.getFromUser().getPhotoThumbUrl())) {
                        incRequestCounter(1);
                        PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                    }
                }
            }
        }

        @Override
        public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "onNotificationList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0 || failed) {
                _workorderThreadManager.wakeUp();
                return;
            }

            Log.v(TAG, "onNotificationList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listNotifications(WebCrawlerService.this, page + 1, true, false);
        }
    };

    private final BundlesWebApi.Listener _bundlesClient_listener = new BundlesWebApi.Listener() {
        @Override
        public void onConnected() {
            _bundleClient.subBundlesWebApi();
        }

        @Override
        public void onGetBundleWorkOrders(WorkOrders workOrders, boolean success, Error error) {
            incrementPendingRequestCounter(-1);
        }
    };

    public class WorkorderDetailWorker extends ThreadManager.ManagedThread {
        private final String TAG = UniqueTag.makeTag("WorkorderDetailWorkerThread");
        private final List<WorkOrder> _work;
        private final Context _context;

        public WorkorderDetailWorker(ThreadManager manager, Context context, List<WorkOrder> workorders) {
            super(manager);
            setName(TAG);
            _context = context;
            _work = workorders;

            start();
        }

        @Override
        public boolean doWork() {
            if (System.currentTimeMillis() - _lastRequestTime > 5000) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Log.v(TAG, e);
                }
                return true;
            }

            WorkOrder workorder = null;
            synchronized (LOCK) {
                if (_work != null && _work.size() > 0) {
                    workorder = _work.remove(0);
                }
            }

            if (workorder == null)
                return false;

            Log.v(TAG, "WorkorderDetailWorker running" + workorder.getId());
            //WorkorderClient.listMessages(WebCrawlerService.this, workorder.getId(), true, false);
            //WorkorderClient.listAlerts(WebCrawlerService.this, workorder.getId(), true);
            //WorkorderClient.listTasks(WebCrawlerService.this, workorder.getId(), true);

/*
            AttachmentFolders attachmentFolders = workorder.getAttachments();
            if (attachmentFolders != null && attachmentFolders.getResults() != null && attachmentFolders.getResults().length > 0) {
                AttachmentFolder[] folders = attachmentFolders.getResults();
                for (AttachmentFolder attachmentFolder : folders) {
                    if (attachmentFolder != null && attachmentFolder.getResults() != null && attachmentFolder.getResults().length > 0) {
                        Attachment[] attachments = attachmentFolder.getResults();
                        for (Attachment attachment : attachments){
                            // TODO, download attachment
                        }
                    }
                }
            }
*/
            return true;
        }

    }
}

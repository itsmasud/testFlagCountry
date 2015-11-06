package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.App;
import com.fieldnation.AsyncTaskEx;
import com.fieldnation.BuildConfig;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

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
    private WorkorderClient _workorderClient;
    private final ThreadManager _workorderThreadManager = new ThreadManager();
    private final List<Workorder> _workorderDetails = new LinkedList<>();

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
                misc.flushLogs(WebCrawlerService.this, 86400000); // 1 day
                Log.v(TAG, "flushing data");
                StoredObject.flush(604800000); // 1 week
                //StoredObject.flush(1000); // 1 week

                Log.v(TAG, "_imageDaysToLive: " + _imageDaysToLive + " haveWifi: " + App.get().haveWifi());
                // only flush if we have wifi, so that the app can get new ones without
                // worrying about cell traffic
                if (_imageDaysToLive > -1 && App.get().haveWifi()) {
                    long days = _imageDaysToLive * 86400000;
                    long cutoff = System.currentTimeMillis();
                    List<StoredObject> list = StoredObject.list(App.getProfileId(), "PhotoCache");

                    Log.v(TAG, "Flushing photos");
                    int count = 0;
                    for (StoredObject obj : list) {
                        if (obj.getLastUpdated() + days < cutoff) {
                            StoredObject.delete(obj);
                            count++;
                        }
                    }

                    list = StoredObject.list(App.getProfileId(), "PhotoCacheCircle");

                    for (StoredObject obj : list) {
                        if (obj.getLastUpdated() + days < cutoff) {
                            StoredObject.delete(obj);
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

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(this);
    }

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
                incRequestCounter(2);
                PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
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


    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected");
            _workorderClient.subList(true);
            _workorderClient.subGet(true);
            _workorderClient.subListMessages(true);

            incrementPendingRequestCounter(2);
            incRequestCounter(2);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.ASSIGNED, 0, true, false);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.COMPLETED, 0, true, false);
        }

        @Override
        public void onList(final List<Workorder> list, final WorkorderDataSelector selector, final int page, boolean failed, boolean isCached) {
            Log.v(TAG, "onWorkorderList, " + selector + ", " + page + ", " + failed + ", " + isCached);

            incrementPendingRequestCounter(-1);

            if (list == null || list.size() == 0 || failed) {
                _workorderThreadManager.wakeUp();
                return;
            }

            Log.v(TAG, "onWorkorderList(" + list.size() + "," + selector.getCall() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            WorkorderClient.list(WebCrawlerService.this, selector, page + 1, true, false);

            Log.v(TAG, "onWorkorderList, Request details");
            for (int i = 0; i < list.size(); i++) {
                Workorder workorder = list.get(i);

                incrementPendingRequestCounter(1);
                incRequestCounter(1);
                WorkorderClient.get(WebCrawlerService.this, workorder.getWorkorderId(), false, true);
                if (workorder.getBundleId() != null && workorder.getBundleId() > 0) {
                    incRequestCounter(1);
                    WorkorderClient.getBundle(WebCrawlerService.this, workorder.getBundleId(), true);
                }
            }
            Log.v(TAG, "onWorkorderList, done");
        }

        @Override
        public void onGet(Workorder workorder, boolean failed) {
            incrementPendingRequestCounter(-1);

            if (failed) return;

            Log.v(TAG, "onDetails " + workorder.getWorkorderId());

            synchronized (LOCK) {
                _workorderDetails.add(workorder);

                Log.v(TAG, "workorder list size " + _workorderDetails.size());
            }
            _workorderThreadManager.wakeUp();
        }

        @Override
        public void onMessageList(long workorderId, List<com.fieldnation.data.workorder.Message> messages, boolean failed) {
            Log.v(TAG, "WorkorderClient.onMessageList");

            incrementPendingRequestCounter(-1);

            if (failed)
                return;

            for (int i = 0; i < messages.size(); i++) {
                incRequestCounter(2);
                com.fieldnation.data.workorder.Message message = messages.get(i);
                PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
            }
            _workorderThreadManager.wakeUp();
        }
    };

    public class WorkorderDetailWorker extends ThreadManager.ManagedThread {
        private String TAG = UniqueTag.makeTag("WorkorderDetailWorkerThread");
        private final List<Workorder> _work;
        private final Context _context;

        public WorkorderDetailWorker(ThreadManager manager, Context context, List<Workorder> workorders) {
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

            Workorder workorder = null;
            synchronized (LOCK) {
                if (_work != null && _work.size() > 0) {
                    workorder = _work.remove(0);
                }
            }

            if (workorder == null)
                return false;

            Log.v(TAG, "WorkorderDetailWorker running" + workorder.getWorkorderId());
            incRequestCounter(3);
            incrementPendingRequestCounter(1);
            WorkorderClient.listMessages(WebCrawlerService.this, workorder.getWorkorderId(), true, false);
            WorkorderClient.listAlerts(WebCrawlerService.this, workorder.getWorkorderId(), true);
            WorkorderClient.listTasks(WebCrawlerService.this, workorder.getWorkorderId(), true);

            // get signatures
            Signature[] sigs = workorder.getSignatureList();
            if (sigs != null && sigs.length > 0) {
                for (Signature sig : sigs) {
                    try {
                        // Log.v(TAG, "getSignature");
                        WorkorderClient.getSignature(_context, workorder.getWorkorderId(), sig.getSignatureId(), true);
                        incRequestCounter(1);
                        //Thread.sleep(1000);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            }

            UploadSlot[] slots = workorder.getUploadSlots();
            if (slots != null && slots.length > 0) {
                for (UploadSlot slot : slots) {
                    UploadedDocument[] docs = slot.getUploadedDocuments();
                    if (docs != null && docs.length > 0) {
                        for (UploadedDocument doc : docs) {
                            try {
                                DocumentClient.downloadDocument(_context, doc.getId(),
                                        doc.getDownloadLink(), doc.getFileName(), true);
                                incRequestCounter(1);
                                //Thread.sleep(1000);
                            } catch (Exception ex) {
                                Log.v(TAG, ex);
                            }
                        }
                    }
                }
            }

            Document[] documents = workorder.getDocuments();
            if (documents != null && documents.length > 0) {
                for (Document doc : documents) {
                    try {
                        DocumentClient.downloadDocument(_context, doc.getDocumentId(),
                                doc.getFilePath(), doc.getFileName(), true);
                        incRequestCounter(1);
                        //Thread.sleep(1000);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            }

            return true;
        }

    }
}

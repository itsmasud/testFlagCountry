package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.service.data.profile.ProfileDataClient;
import com.fieldnation.service.data.signature.SignatureClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class WebCrawlerService extends Service {
    private static final String TAG = "WebCrawlerService";
    private final Object LOCK = new Object();

    private ProfileDataClient _profileClient;
    private WorkorderClient _workorderClient;
    private final ThreadManager _workorderThreadManager = new ThreadManager();
    private final List<Workorder> _workorderDetails = new LinkedList<>();

    private boolean _haveProfile = false;
    private long _pendingRequestCounter = 0;
    private long _requestCounter = 0;
    private boolean _skipProfileImages = true;


    public WebCrawlerService() {
        super();
        Log.v(TAG, "WebCrawlerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    private synchronized void incrementPendingRequestCounter(int val) {
        _pendingRequestCounter += val;
        Log.v(TAG, "_pendingRequestCounter = " + _pendingRequestCounter);
    }

    private synchronized void incRequestCounter(int val) {
        _requestCounter += val;
        Log.v(TAG, "_requestCounter = " + _requestCounter);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _workorderClient.disconnect(this);
        _profileClient.disconnect(this);
        _workorderThreadManager.shutdown();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

        // we're not allowed to run, stop
        if (!settings.getBoolean(getString(R.string.pref_key_sync_enabled), false)) {
            Log.v(TAG, "sync disabled, quiting");
            return START_NOT_STICKY;
        }

        // if clock is not set, set it
        long runTime = settings.getLong(getString(R.string.pref_key_sync_start_time), 180);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, (int) (runTime / 60));
        cal.set(Calendar.MINUTE, (int) (runTime % 60));

        long nextTime = cal.getTimeInMillis();
        if (nextTime < System.currentTimeMillis()) {
            nextTime += 86400000;
        }

        AlarmBroadcastReceiver.registerCrawlerAlarm(this, nextTime);
        Log.v(TAG, "register sync alarm " + ISO8601.fromUTC(nextTime));

        // if already running, then return
        if (_pendingRequestCounter > 0) {
            Log.v(TAG, "already running, stopping");
            return START_STICKY;
        }

        // if not running then
        if (intent.hasExtra("IS_ALARM")) {
            //      check the following
            //      check last start time against current time
            //      check current time against time rule
            //      check wifi rules
            //      check power rules
            //      get profile image rule
            //      if all is ok then start up

            Log.v(TAG, "alarm triggered");

            // TODO do the purge... cause we got nowhere else to do it right now
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    misc.flushLogs(WebCrawlerService.this, 86400000); // 1 day
                    StoredObject.flush(WebCrawlerService.this, 604800000); // 1 week
                    return null;
                }
            }.executeEx();

            _skipProfileImages = settings.getBoolean(getString(R.string.pref_key_sync_skip_profile_images), true);

            startStuff();

            return START_STICKY;
        }

        Log.v(TAG, "Do nothing");

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startStuff() {
        Log.v(TAG, "startStuff");

        _workorderThreadManager.addThread(new WorkorderDetailWorker(_workorderThreadManager, this, _workorderDetails));

        _profileClient = new ProfileDataClient(_profileClient_listener);
        _profileClient.connect(this);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(this);
    }

    private final ProfileDataClient.Listener _profileClient_listener = new ProfileDataClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_profileClient_listener.onConnected");
            _profileClient.registerAllMessages(true);
            _profileClient.registerAllNotifications(true);
            _profileClient.registerProfile(true);

            incrementPendingRequestCounter(3);
            incRequestCounter(3);
            ProfileDataClient.getProfile(WebCrawlerService.this, true);
            ProfileDataClient.getAllMessages(WebCrawlerService.this, 0, true);
            ProfileDataClient.getAllNotifications(WebCrawlerService.this, 0, true);
        }

        @Override
        public void onProfile(Profile profile) {
            incrementPendingRequestCounter(-1);
            Log.v(TAG, "onProfile " + _haveProfile);
            if (!_haveProfile) {
                Log.v(TAG, "onProfile");
                if (!_skipProfileImages) {
                    incRequestCounter(2);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, profile.getPhoto().getLarge(), true, true);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, profile.getPhoto().getThumb(), true, true);
                }
                _haveProfile = true;
            }
        }

        @Override
        public void onAllMessagesPage(List<Message> list, int page) {
            Log.v(TAG, "onAllMessagesPage");

            incrementPendingRequestCounter(-1);

            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onAllMessagesPage(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileDataClient.getAllMessages(WebCrawlerService.this, page + 1, true);

            if (!_skipProfileImages) {
                for (int i = 0; i < list.size(); i++) {
                    Message message = list.get(i);
                    incRequestCounter(2);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                }
            }
        }

        @Override
        public void onAllNotificationPage(List<Notification> list, int page) {
            Log.v(TAG, "onAllNotificationPage");
            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onAllNotificationPage(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileDataClient.getAllNotifications(WebCrawlerService.this, page + 1, true);
        }
    };


    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected");
            _workorderClient.subList(true);
            _workorderClient.subGet(true);
            _workorderClient.registerDeliverableList(true);

            incrementPendingRequestCounter(3);
            incRequestCounter(3);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.ASSIGNED, 0, true);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.CANCELED, 0, true);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.COMPLETED, 0, true);
        }

        @Override
        public void onWorkorderList(List<Workorder> list, WorkorderDataSelector selector, int page) {
            Log.v(TAG, "onWorkorderList");
            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0) {
                return;
            }
            Log.v(TAG, "onWorkorderList(" + list.size() + "," + selector.getCall() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            WorkorderClient.list(WebCrawlerService.this, selector, page + 1, true);

            Log.v(TAG, "onWorkorderList, Request details");
            for (int i = 0; i < list.size(); i++) {
                Workorder workorder = list.get(i);

                incrementPendingRequestCounter(1);
                incRequestCounter(2);
                WorkorderClient.get(WebCrawlerService.this, workorder.getWorkorderId(), true);
                WorkorderClient.listMessages(WebCrawlerService.this, workorder.getWorkorderId(), true);
                if (workorder.getBundleId() != null && workorder.getBundleId() > 0) {
                    incRequestCounter(1);
                    WorkorderClient.requestBundle(WebCrawlerService.this, workorder.getBundleId(), true);
                }

                // get notifications
                // get tasks

            }
            Log.v(TAG, "onWorkorderList, done");
        }

        @Override
        public void onDetails(Workorder workorder) {
            Log.v(TAG, "onDetails " + workorder.getWorkorderId());
            incrementPendingRequestCounter(-1);
            synchronized (LOCK) {
                _workorderDetails.add(workorder);

                Log.v(TAG, "workorder list size " + _workorderDetails.size());
            }
            _workorderThreadManager.wakeUp();
        }

        @Override
        public void onMessageList(long workorderId, List<com.fieldnation.data.workorder.Message> messages) {
            super.onMessageList(workorderId, messages);

            incrementPendingRequestCounter(-1);

            if (!_skipProfileImages) {
                for (int i = 0; i < messages.size(); i++) {
                    incRequestCounter(2);
                    com.fieldnation.data.workorder.Message message = messages.get(i);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    PhotoDataClient.dispatchGetPhoto(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                }
            }
        }

        @Override
        public void onDeliverableList(List<Deliverable> list, long workorderId) {
            Log.v(TAG, "onDeliverableList");
            incrementPendingRequestCounter(-1);
            for (int i = 0; i < list.size(); i++) {
                Deliverable d = list.get(i);
                incRequestCounter(1);
                WorkorderClient.requestDownloadDeliverable(WebCrawlerService.this, workorderId,
                        d.getWorkorderUploadId(), d.getStorageSrc(), true);
            }

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
            Workorder workorder = null;
            synchronized (LOCK) {
                if (_work != null && _work.size() > 0) {
                    workorder = _work.remove(0);
                }
            }

            if (workorder == null)
                return false;

            // get signatures
            Signature[] sigs = workorder.getSignatureList();
            if (sigs != null && sigs.length > 0) {
                for (int i = 0; i < sigs.length; i++) {
                    Log.v(TAG, "getSignature");
                    incRequestCounter(1);
                    SignatureClient.get(_context, workorder.getWorkorderId(), sigs[i].getSignatureId(), true);
                }
            }

            UploadSlot[] slots = workorder.getUploadSlots();
            if (slots != null && slots.length > 0) {
                for (int i = 0; i < slots.length; i++) {
                    UploadSlot slot = slots[i];

                    UploadedDocument[] docs = slot.getUploadedDocuments();
                    if (docs != null && docs.length > 0) {
                        for (int j = 0; j < docs.length; j++) {
                            UploadedDocument doc = docs[j];

                            // todo request document
                            //doc.getDownloadLink();
                            //
                        }
                    }
                }
            }

            Deliverable[] deliverables = workorder.getDeliverables();
            if (deliverables != null && deliverables.length > 0) {
                incrementPendingRequestCounter(1);
                incRequestCounter(1);
                WorkorderClient.requestDeliverableList(WebCrawlerService.this, workorder.getWorkorderId(), true);
            }
            return true;
        }

    }
}

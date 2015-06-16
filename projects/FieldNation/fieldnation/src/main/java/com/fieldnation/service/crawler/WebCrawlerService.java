package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

        scheduleNext();

        // if already running, then return
        if (_pendingRequestCounter > 0) {
            Log.v(TAG, "already running, stopping");
            return START_STICKY;
        }

        // if not running then
        if (intent != null && intent.hasExtra("IS_ALARM")) {
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

            runCrawler();

            return START_STICKY;
        }

        Log.v(TAG, "Do nothing");

        return START_NOT_STICKY;
    }

    private synchronized void incrementPendingRequestCounter(int val) {
        _pendingRequestCounter += val;
//        if (_pendingRequestCounter % 5 == 0) {
            Log.v(TAG, "_pendingRequestCounter = " + _pendingRequestCounter);
            Log.v(TAG, "_workorderDetails.size() = " + _workorderDetails.size());
//        }

        if (_pendingRequestCounter < 50) {
            _workorderThreadManager.wakeUp();
        }

        if (_pendingRequestCounter == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (_pendingRequestCounter == 0) {
                        scheduleNext();
                        stopSelf();
                    } else {
                        incrementPendingRequestCounter(0);
                    }
                }
            }, 30000);
        }
    }

    private synchronized void incRequestCounter(int val) {
        _requestCounter += val;
        if (_requestCounter % 5 == 0)
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

    private void scheduleNext() {
        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

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
    }


    public void runCrawler() {
        Log.v(TAG, "runCrawler");

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
            ProfileClient.get(WebCrawlerService.this, 0, true);
            ProfileClient.listMessages(WebCrawlerService.this, 0, true);
            ProfileClient.listNotifications(WebCrawlerService.this, 0, true);
        }

        @Override
        public void onGet(Profile profile) {
            Log.v(TAG, "ProfileClient.onGet " + _haveProfile);
            if (!_haveProfile) {
                incrementPendingRequestCounter(-1);
                Log.v(TAG, "ProfileClienton.onGet");
                if (!_skipProfileImages) {
                    incRequestCounter(2);
                    PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getLarge(), true, true);
                    PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getThumb(), true, true);
                }
                _haveProfile = true;
            }
        }

        @Override
        public void onMessageList(List<Message> list, int page) {
            Log.v(TAG, "ProfileClient.onMessageList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0) {
                _workorderThreadManager.wakeUp();
                return;
            }
            Log.v(TAG, "onMessageList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listMessages(WebCrawlerService.this, page + 1, true);

            if (!_skipProfileImages) {
                for (int i = 0; i < list.size(); i++) {
                    Message message = list.get(i);
                    incRequestCounter(2);
                    PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                }
            }
        }

        @Override
        public void onNotificationList(List<Notification> list, int page) {
            Log.v(TAG, "onNotificationList");
            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0) {
                _workorderThreadManager.wakeUp();
                return;
            }
            Log.v(TAG, "onNotificationList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listNotifications(WebCrawlerService.this, page + 1, true);
        }
    };


    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected");
            _workorderClient.subList(true);
            _workorderClient.subGet(true);
            _workorderClient.subListMessages(true);

            incrementPendingRequestCounter(3);
            incRequestCounter(3);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.ASSIGNED, 0, true);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.CANCELED, 0, true);
            WorkorderClient.list(WebCrawlerService.this, WorkorderDataSelector.COMPLETED, 0, true);
        }

        @Override
        public void onList(final List<Workorder> list, final WorkorderDataSelector selector, final int page) {
            Log.v(TAG, "onWorkorderList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0) {
                _workorderThreadManager.wakeUp();
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
        public void onGet(Workorder workorder) {
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
            Log.v(TAG, "WorkorderClient.onMessageList");

            incrementPendingRequestCounter(-1);

            if (!_skipProfileImages) {
                for (int i = 0; i < messages.size(); i++) {
                    incRequestCounter(2);
                    com.fieldnation.data.workorder.Message message = messages.get(i);
                    PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                }
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
            Log.v(TAG, "doWork");

            if (_pendingRequestCounter > 50) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
            WorkorderClient.listMessages(WebCrawlerService.this, workorder.getWorkorderId(), true);
            WorkorderClient.listAlerts(WebCrawlerService.this, workorder.getWorkorderId(), true);
            WorkorderClient.listTasks(WebCrawlerService.this, workorder.getWorkorderId(), true);

            // get signatures
            Signature[] sigs = workorder.getSignatureList();
            if (sigs != null && sigs.length > 0) {
                for (Signature sig : sigs) {
//                    Log.v(TAG, "getSignature");
                    incRequestCounter(1);
                    WorkorderClient.getSignature(_context, workorder.getWorkorderId(), sig.getSignatureId(), true);
                }
            }

            UploadSlot[] slots = workorder.getUploadSlots();
            if (slots != null && slots.length > 0) {
                for (UploadSlot slot : slots) {
                    UploadedDocument[] docs = slot.getUploadedDocuments();
                    if (docs != null && docs.length > 0) {
                        for (UploadedDocument doc : docs) {
                            incRequestCounter(1);
                            DocumentClient.downloadDocument(_context, doc.getId(),
                                    doc.getDownloadLink(), doc.getFileName(), true);
                        }
                    }
                }
            }

            Document[] documents = workorder.getDocuments();
            if (documents != null && documents.length > 0) {
                for (Document doc : documents) {
                    incRequestCounter(1);
                    DocumentClient.downloadDocument(_context, doc.getDocumentId(), doc.getFilePath(), doc.getFileName(), true);
                }
            }

            return true;
        }

    }
}

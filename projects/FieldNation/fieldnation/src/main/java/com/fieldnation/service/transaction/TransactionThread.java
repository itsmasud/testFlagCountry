package com.fieldnation.service.transaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.NotificationDef;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.fnanalytics.Timing;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnhttpjson.HttpJson;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.auth.OAuth;

import java.text.ParseException;

/**
 * Created by itssh on 3/23/2016.
 * <p/>
 * This class executes requests that are stored in the transaction queue
 */
class TransactionThread extends ThreadManager.ManagedThread {
    private final String TAG = UniqueTag.makeTag("TransactionThread");
    private final Object SYNC_LOCK = new Object();

    private static final long[] RETRY_TIMES = new long[]{30000, 120000, 300000, 600000};
    //private static final long[] RETRY_TIMES = new long[]{5000, 5000, 5000, 5000};

    private final WebTransactionSystem _service;

    private boolean _syncThread = false;
    private boolean _allowSync = true;
    private long _syncCheckCoolDown = 0;

    public boolean _isFirstThread = false;

    private static JsonObject TEST_QUERY;

    static {
        try {
            TEST_QUERY = new HttpJsonBuilder().path("http://www.fieldnation.com").build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public TransactionThread(ThreadManager manager, WebTransactionSystem service, boolean syncThread) {
        super(manager);
        setName(TAG);
        _service = service;
        _syncThread = syncThread;
        start();
    }

    private static class MyProgressListener implements HttpJson.ProgressListener {
        public WebTransaction trans;

        public MyProgressListener() {
        }

        @Override
        public void progress(long pos, long size, long time) {
            WebTransactionDispatcher.progress(ContextProvider.get(), trans.getListenerName(), trans, pos, size, time);
        }
    }

    private final MyProgressListener _http_progress = new MyProgressListener();

    // when called we are doing a retry on a query.
    private long getRetry(int count) {
        if (count < 0)
            count = 0;

        if (count >= RETRY_TIMES.length)
            count = RETRY_TIMES.length - 1;

        Log.v(TAG, "getRetry " + RETRY_TIMES[count]);

        return RETRY_TIMES[count];
    }

    @Override
    public boolean doWork() {
        // try to get a transaction
        if (!App.get().isConnected()) {
            if (!_isFirstThread) {
                Log.v(TAG, "Connection down, skipping");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                return false;
            }

            Log.v(TAG, "Testing connection");
            try {
                HttpJson.run(App.get(), TEST_QUERY);
                AppMessagingClient.networkConnected();
                Log.v(TAG, "Testing connection... success!");
            } catch (Exception e) {
                Log.v(TAG, "Testing connection... failed!");
                AppMessagingClient.networkDisconnected();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                return false;
            }
        }

        WebTransaction trans = null;
        try {
            trans = WebTransaction.getNext(_syncThread && allowSync(), _service.isAuthenticated(),
                    _syncThread ? Priority.LOW : Priority.NORMAL);
        } catch (SQLiteFullException ex) {
            ToastClient.toast(ContextProvider.get(), "Your device is full. Please free up space.", Toast.LENGTH_LONG);
            return false;
        }

        // if failed, then exit
        if (trans == null) {
            return false;
        }

        if (trans.getUUID() != null) {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(trans.getUUID()))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.START, "Transaction Processing"))
                    .build());
        }

        // debug if have key, output
        //if (!misc.isEmptyOrNull(trans.getKey())) {
        //Log.v(TAG, "Key: " + trans.getKey());
        //}

        // Load the request, and apply authentication
        JsonObject request = null;
        try {
            request = new JsonObject(trans.getRequestString());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        if (request == null) {
            // should never happen!
            WebTransaction.delete(trans.getId());
            return false;
        }

        String listenerName = trans.getListenerName();
        HttpResult result = null;

        int notifId = 0;
        NotificationDefinition notifStart = null;
        NotificationDefinition notifSuccess = null;
        NotificationDefinition notifFailed = null;
        NotificationDefinition notifRetry = null;

        Stopwatch stopwatch = null;
        // apply authentication if needed
        if (trans.useAuth()) {
            OAuth auth = _service.getAuth();

            if (auth == null) {
                Log.v(TAG, "auth == null");
                AuthClient.requestCommand();
                trans.requeue(4000);
//                trans.requeue(getRetry(trans.getTryCount()));
                if (!misc.isEmptyOrNull(listenerName))
                    WebTransactionDispatcher.paused(App.get(), listenerName, trans);
                return false;
            }

            if (auth.getAccessToken() == null) {
                Log.v(TAG, "accessToken is null");
                AuthClient.invalidateCommand();
                trans.requeue(4000);
//                trans.requeue(getRetry(trans.getTryCount()));
                if (!misc.isEmptyOrNull(listenerName))
                    WebTransactionDispatcher.paused(App.get(), listenerName, trans);
                return false;
            }

            if (!_service.isAuthenticated()) {
                Log.v(TAG, "skip no auth");
                trans.requeue(4000);
//                trans.requeue(getRetry(trans.getTryCount()));
                if (!misc.isEmptyOrNull(listenerName))
                    WebTransactionDispatcher.paused(App.get(), listenerName, trans);
                return false;
            }

            try {
                if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                    request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                }
                request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                auth.applyToRequest(request);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        if (trans.getNotificationId() != -1) {
            notifId = trans.getNotificationId();
            notifStart = trans.getNotificationStart();
            notifSuccess = trans.getNotificationSuccess();
            notifFailed = trans.getNotificationFailed();
            notifRetry = trans.getNotificationRetry();
            generateNotification(notifId, notifStart);
        }

        Log.v(TAG, request.display());

        if (!misc.isEmptyOrNull(listenerName)) {
            WebTransactionDispatcher.start(App.get(), listenerName, trans);
        }

        // **** perform request ****
        Exception exception = null;
        try {
            _http_progress.trans = trans;
            stopwatch = new Stopwatch(true);
            result = HttpJson.run(App.get(), request, _http_progress);
            stopwatch.pause();
            try {
                Log.v(TAG, "ResponseCode: " + result.getResponseCode());
                Log.v(TAG, "ResponseMessage: " + result.getResponseMessage());
                // this can cause OOM errors, only uncomment when trying to debug something
                //if (!result.isFile() && result.getString() != null) {
                //Log.v(TAG, "Result: " + result.getString());
                //}
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } catch (Exception ex) {
            exception = ex;
            Log.v(TAG, ex);
        }

        if (!misc.isEmptyOrNull(trans.getTimingKey())) {
            recordTiming(stopwatch, trans.getTimingKey());
        }
        // At this point we will have trans, exception and/or result

        WebTransactionListener.Result wresult = WebTransactionDispatcher.complete(App.get(),
                trans.getListenerName(), trans, result, exception);

        // do what the result says
        switch (wresult) {
            case DELETE:
                generateNotification(notifId, notifFailed);
                WebTransaction.delete(trans.getId());
                break;
            case CONTINUE:
                if (trans.getUUID() != null) {
                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpTracingContext(trans.getUUID()))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Transaction Processing"))
                            .build());
                }
                generateNotification(notifId, notifSuccess);
                WebTransaction.delete(trans.getId());
                break;
            case RETRY:
                Log.v(TAG, "3");
                if (!misc.isEmptyOrNull(listenerName))
                    WebTransactionDispatcher.paused(App.get(), listenerName, trans);

                generateNotification(notifId, notifRetry);
                trans.requeue(getRetry(trans.getTryCount()));

                break;
            case ZOMBIE:
                generateNotification(notifId, notifFailed);
                trans.setZombie();
                trans.save();
                break;
        }
        return true;
    }

    private void recordTiming(Stopwatch stopwatch, String timingKey) {
        stopwatch.unpause();
        Tracker.timing(ContextProvider.get(),
                new Timing.Builder()
                        .tag(AnswersWrapper.TAG)
                        .category("Web Timing")
                        .label(timingKey)
                        .timing((int) stopwatch.finish())
                        .build());

        Log.v(TAG, timingKey + " run time: " + stopwatch.getTime());
    }

    private static void generateNotification(int notifyId, NotificationDefinition notif) {
        if (notif == null)
            return;

        NotificationManager mNotifyMgr = (NotificationManager) ContextProvider.get().getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(ContextProvider.get(), NotificationDef.OTHER_CHANNEL)
                    .setLargeIcon((Bitmap) null)
                    .setSmallIcon(notif.icon)
                    .setContentTitle(notif.title)
                    .setTicker(notif.ticker)
                    .setContentText(notif.body)
                    .build();

            mNotifyMgr.notify(notifyId, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ContextProvider.get())
                    .setLargeIcon(null)
                    .setSmallIcon(notif.icon)
                    .setContentTitle(notif.title)
                    .setTicker(notif.ticker)
                    .setContentText(notif.body);

            mNotifyMgr.notify(notifyId, mBuilder.build());
        }
    }

    private boolean allowSync() {
        synchronized (SYNC_LOCK) {
            if (_syncCheckCoolDown < System.currentTimeMillis()) {
                //Log.v(TAG, "Running allowSync");
                _allowSync = true;

                SharedPreferences settings = App.get().getSharedPreferences();

                boolean requireWifi = settings.getBoolean(App.get().getString(R.string.pref_key_sync_require_wifi), true);
                boolean requirePower = settings.getBoolean(App.get().getString(R.string.pref_key_sync_require_power), true);
                boolean haveWifi = App.get().haveWifi();

                if (requireWifi && !haveWifi) {
                    _allowSync = false;
                } else {
                    boolean pluggedIn = App.get().isCharging();
                    if (requirePower && !pluggedIn) {
                        _allowSync = false;
                    }
                }
                _syncCheckCoolDown = System.currentTimeMillis() + 60000;
            }
            return _allowSync;
        }
    }
}
package com.fieldnation.service.transaction;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.MSService;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLProtocolException;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * this class does two things.
 * 1) Accepts transactions from the rest of the application
 * 2) processes transactions from the queue until they are complete
 */
public class WebTransactionService extends MSService implements WebTransactionConstants {
    private static final String TAG = "WebTransactionService";

    private final Object AUTH_LOCK = new Object();
    private final Object SYNC_LOCK = new Object();

    private OAuth _auth;
    private AuthTopicClient _authTopicClient;
    private GlobalTopicClient _globalTopicClient;
    private boolean _isAuthenticated = false;
    private ThreadManager _manager;
    private boolean _allowSync = true;
    private long _syncCheckCoolDown = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        WebTransaction.saveOrphans(this);

        int threadCount = 4;
        if (App.get().isLowMemDevice()) {
            threadCount = 4;
        } else {
            threadCount = 8;
        }

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(App.get());

        _globalTopicClient = new GlobalTopicClient(_globalTopic_listener);
        _globalTopicClient.connect(App.get());

        _manager = new ThreadManager();
        _manager.addThread(new TransactionThread(_manager, this, false)); // 0
        _manager.addThread(new TransactionThread(_manager, this, true)); // 1
        for (int i = 2; i < threadCount; i++) {
            // every other can do sync
            _manager.addThread(new TransactionThread(_manager, this, i % 2 == 1));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean isStillWorking() {
        return WebTransaction.count(this) > 0;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (_authTopicClient != null && _authTopicClient.isConnected())
            _authTopicClient.disconnect(App.get());

        if (_globalTopicClient != null && _globalTopicClient.isConnected())
            _globalTopicClient.disconnect(App.get());

        _manager.shutdown();
        super.onDestroy();
    }

    private void setAuth(OAuth auth) {
        Log.v(TAG, "setAuth start");
        synchronized (AUTH_LOCK) {
            _auth = auth;
        }
        _manager.wakeUp();
        Log.v(TAG, "setAuth end");
    }

    private OAuth getAuth() {
        Log.v(TAG, "getAuth");
        synchronized (AUTH_LOCK) {
            return _auth;
        }
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    private boolean allowSync() {
        synchronized (SYNC_LOCK) {
            // TODO calculate by collecting config information and compare to phone state
            if (_syncCheckCoolDown < System.currentTimeMillis()) {
                Log.v(TAG, "Running allowSync");
                _allowSync = true;

                SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                        Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

                boolean requireWifi = settings.getBoolean(getString(R.string.pref_key_sync_require_wifi), true);
                boolean requirePower = settings.getBoolean(getString(R.string.pref_key_sync_require_power), true);
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

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subNetworkConnect();
        }

        @Override
        public void onNetworkConnect() {
            _manager.wakeUp();
        }
    };

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "AuthTopicClient.onConnected");
            _authTopicClient.subAuthStateChange();
            AuthTopicClient.requestCommand(WebTransactionService.this);
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "AuthTopicClient.onAuthenticated");
            setAuth(oauth);
            _isAuthenticated = true;
            _manager.wakeUp();
        }

        @Override
        public void onNotAuthenticated() {
            _isAuthenticated = false;
        }
    };

    @Override
    public void addIntent(List<Intent> intents, Intent intent) {
        if (intent.getBooleanExtra(PARAM_IS_SYNC, false)) {
            intents.add(intent);
        } else {
            intents.add(0, intent);
        }
    }

    @Override
    public void processIntent(Intent intent) {
        Log.v(TAG, "processIntent start");
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();

                if (extras.containsKey(PARAM_KEY) && WebTransaction.keyExists(this,
                        extras.getString(PARAM_KEY))) {
                    Log.v(TAG, "processIntent end duplicate " + extras.getString(PARAM_KEY));
                    _manager.wakeUp();
                    return;
                }

                Log.v(TAG, "processIntent building transaction");
                WebTransaction transaction = WebTransaction.put(this,
                        (Priority) extras.getSerializable(PARAM_PRIORITY),
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        extras.getBoolean(PARAM_IS_SYNC),
                        extras.getByteArray(PARAM_REQUEST),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));

                Log.v(TAG, "processIntent building transforms");
                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Parcelable[] transforms = extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = (Bundle) transforms[i];
                        Transform.put(this, transaction.getId(), transform);
                    }
                }

                Log.v(TAG, "processIntent saving transaction");
                transaction.setState(WebTransaction.State.IDLE);
                transaction.save(this);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        _manager.wakeUp();

        Log.v(TAG, "processIntent end");
    }


    private void generateNotification(int notifyId, NotificationDefinition notif) {
        if (notif == null)
            return;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(notif.icon)
                .setContentTitle(notif.title)
                .setTicker(notif.ticker)
                .setContentText(notif.body);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Log.v(TAG, "notification created");

        mNotifyMgr.notify(notifyId, mBuilder.build());
    }

    private static JsonObject TEST_QUERY;

    static {
        try {
            TEST_QUERY = new HttpJsonBuilder().path("http://www.fieldnation.com").build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class TransactionThread extends ThreadManager.ManagedThread {
        private String TAG = UniqueTag.makeTag("TransactionThread");
        private Context context;
        private boolean _syncThread = false;

        public TransactionThread(ThreadManager manager, Context context, boolean syncThread) {
            super(manager);
            setName(TAG);
            this.context = context;
            _syncThread = syncThread;
            start();
        }

        @Override
        public boolean doWork() {
            // try to get a transaction

            if (!App.get().isConnected()) {
                Log.v(TAG, "Testing connection");
                try {
                    HttpJson.run(TEST_QUERY);
                    GlobalTopicClient.networkConnected(context);
                    Log.v(TAG, "Testing connection... success!");
                } catch (Exception e) {
                    Log.v(TAG, "Testing connection... failed!");
                    GlobalTopicClient.networkDisconnected(context);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                    return false;
                }
            }

            //Log.v(TAG, "Trans Count: " + WebTransaction.count(context));
            WebTransaction trans = WebTransaction.getNext(context, _syncThread && allowSync(), _isAuthenticated);

            // if failed, then exit
            if (trans == null) {
                return false;
            }

            // debug if have key, output
            if (!misc.isEmptyOrNull(trans.getKey())) {
                Log.v(TAG, "Key: " + trans.getKey());
            }

            // Load the request, and apply authentication
            JsonObject request = null;
            try {
                request = new JsonObject(trans.getRequestString());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            if (request == null) {
                // should never happen!
                WebTransaction.delete(context, trans.getId());
                return false;
            }

            String handlerName = null;
            HttpResult result = null;

            int notifId = 0;
            NotificationDefinition notifStart = null;
            NotificationDefinition notifSuccess = null;
            NotificationDefinition notifFailed = null;
            NotificationDefinition notifRetry = null;

            try {
                // apply authentication if needed
                if (trans.useAuth()) {
                    OAuth auth = getAuth();
                    if (!_isAuthenticated) {
                        Log.v(TAG, "skip no auth");
                        trans.requeue(context);
                        return false;
                    }

                    if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                        request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                    }
                    request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                    auth.applyToRequest(request);
                }


                if (request.has(HttpJsonBuilder.PARAM_NOTIFICATION_ID)) {
                    notifId = request.getInt(HttpJsonBuilder.PARAM_NOTIFICATION_ID);
                    notifStart = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_START));
                    notifSuccess = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_SUCCESS));
                    notifFailed = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_FAILED));
                    notifRetry = NotificationDefinition.fromJson(request.getJsonObject(HttpJsonBuilder.PARAM_NOTIFICATION_RETRY));
                    generateNotification(notifId, notifStart);
                }

                Log.v(TAG, request.display());

                handlerName = trans.getHandlerName();

                if (!misc.isEmptyOrNull(handlerName)) {
                    WebTransactionHandler.Result wresult = WebTransactionHandler.startTransaction(context, handlerName, trans);
                }

                // **** perform request ****
                result = HttpJson.run(request);

                // debug output
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

                // **** Error handling ****
                // check for invalid auth
                if (!result.isFile()
                        && "You must provide a valid OAuth token to make a request".equals(result.getString())) {
                    Log.v(TAG, "Reauth");
                    _isAuthenticated = false;
                    AuthTopicClient.invalidateCommand(context);
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                    AuthTopicClient.requestCommand(context);
                    return true;

                } else if (result.getResponseCode() == 400) {
                    // Bad request
                    // need to report this
                    // need to re-auth?
                    if ("You don't have permission to see this workorder".equals(result.getString())) {
                        WebTransactionHandler.failTransaction(context, handlerName, trans, result, null);
                        WebTransaction.delete(context, trans.getId());
                    } else if (result.getResponseMessage().contains("Bad Request")) {
                        WebTransactionHandler.failTransaction(context, handlerName, trans, result, null);
                        WebTransaction.delete(context, trans.getId());
                    } else {
                        Log.v(TAG, "1");
                        AuthTopicClient.invalidateCommand(context);
                        transRequeueNetworkDown(trans, notifId, notifRetry);
                        AuthTopicClient.requestCommand(context);
                    }

                } else if (result.getResponseCode() == 401) {
                    // 401 usually means bad auth token
                    Log.v(TAG, "Reauth 2");
                    _isAuthenticated = false;
                    AuthTopicClient.invalidateCommand(context);
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                    AuthTopicClient.requestCommand(context);
                    return true;

                } else if (result.getResponseCode() == 404) {
                    // not found?... error
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result, null);
                    WebTransaction.delete(context, trans.getId());
                    generateNotification(notifId, notifFailed);
                    return true;

                    // usually means code is being updated on the server
                } else if (result.getResponseCode() == 502) {
                    Log.v(TAG, "2");
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                    AuthTopicClient.requestCommand(context);
                    return true;

                } else if (result.getResponseCode() / 100 != 2) {
                    Log.v(TAG, "3");
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result, null);
                    WebTransaction.delete(context, trans.getId());
                    generateNotification(notifId, notifFailed);
                    return true;
                }

                Log.v(TAG, "Passed response error checks");

                GlobalTopicClient.networkConnected(context);

                if (!misc.isEmptyOrNull(handlerName)) {
                    WebTransactionHandler.Result wresult = WebTransactionHandler.completeTransaction(
                            context, handlerName, trans, result);

                    switch (wresult) {
                        case ERROR:
                            generateNotification(notifId, notifFailed);
                            WebTransactionHandler.failTransaction(context, handlerName, trans, result, null);
                            WebTransaction.delete(context, trans.getId());
                            break;
                        case FINISH:
                            generateNotification(notifId, notifSuccess);
                            WebTransaction.delete(context, trans.getId());
                            break;
                        case REQUEUE:
                            Log.v(TAG, "3");
                            transRequeueNetworkDown(trans, notifId, notifRetry);
                            break;
                    }
                }
            } catch (MalformedURLException | FileNotFoundException ex) {
                Log.v(TAG, "4");
                WebTransactionHandler.failTransaction(context, handlerName, trans, result, ex);
                WebTransaction.delete(context, trans.getId());
                generateNotification(notifId, notifFailed);

            } catch (SSLProtocolException | UnknownHostException | ConnectException | SocketTimeoutException | EOFException ex) {
                Log.v(TAG, "5");
                transRequeueNetworkDown(trans, notifId, notifRetry);

            } catch (SSLException ex) {
                if (ex.getMessage().contains("Broken pipe")) {
                    Log.v(TAG, "6");
                    ToastClient.toast(context, "File too large to upload", Toast.LENGTH_LONG);
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result, ex);
                    WebTransaction.delete(context, trans.getId());
                    generateNotification(notifId, notifFailed);
                } else {
                    Log.v(TAG, "7");
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                }

            } catch (IOException ex) {
                Log.v(TAG, "8");
                transRequeueNetworkDown(trans, notifId, notifRetry);

            } catch (Exception ex) {
                Log.v(TAG, "9");
                if (ex.getMessage() != null && ex.getMessage().contains("ETIMEDOUT")) {
                    transRequeueNetworkDown(trans, notifId, notifRetry);
                } else {
                    // no freaking clue
                    Debug.logException(ex);
                    Log.v(TAG, ex);
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result, ex);
                    WebTransaction.delete(context, trans.getId());
                    generateNotification(notifId, notifFailed);
                }
            }
            Log.v(TAG, "10");
            return true;
        }

        private void transRequeueNetworkDown(WebTransaction trans, int notifId, NotificationDefinition notif) {
            Log.v(TAG, "transRequeueNetworkDown");
            misc.printStackTrace("transRequeueNetworkDown");
            generateNotification(notifId, notif);
            GlobalTopicClient.networkDisconnected(context);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            trans.requeue(context);
        }
    }
}

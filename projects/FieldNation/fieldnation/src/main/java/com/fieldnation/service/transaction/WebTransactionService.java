package com.fieldnation.service.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.App;
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
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * this class does two things.
 * 1) Accepts transactions from the rest of the application
 * 2) processes transactions from the queue until they are complete
 */
public class WebTransactionService extends MSService implements WebTransactionConstants {
    private static final String TAG = "WebTransactionService";

    private OAuth _auth;
    private AuthTopicClient _authTopicClient;
    private boolean _isAuthenticated = false;
    private ThreadManager _manager;
    private boolean _allowSync = true;
    private long _syncCheckCoolDown = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        int threadCount = 4;
        if (App.get().getMemoryClass() <= 64) {
            threadCount = 4;
        } else {
            threadCount = 8;
        }

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(this);

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
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _authTopicClient.disconnect(this);
        _manager.shutdown();
        super.onDestroy();
    }

    private void setAuth(OAuth auth) {
        Log.v(TAG, "setAuth");
        synchronized (TAG) {
            _auth = auth;
        }
    }

    private OAuth getAuth() {
        Log.v(TAG, "getAuth");
        synchronized (TAG) {
            return _auth;
        }
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    private synchronized boolean allowSync() {
        // TODO calculate by collecting config information and compare to phone state
        if (_syncCheckCoolDown < System.currentTimeMillis()) {
            Stopwatch watch = new Stopwatch(true);
            _allowSync = true;

            SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                    Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

            boolean requireWifi = settings.getBoolean(getString(R.string.pref_key_sync_require_wifi), true);
            boolean requirePower = settings.getBoolean(getString(R.string.pref_key_sync_require_power), true);
            boolean haveWifi = App.get().haveWifi();

            Log.v(TAG, "HaveWifi " + haveWifi);

            if (requireWifi && !haveWifi) {
                _allowSync = false;
            } else {
                boolean pluggedIn = App.get().isCharging();
                Log.v(TAG, "HavePower " + pluggedIn);
                if (requirePower && !pluggedIn) {
                    _allowSync = false;
                }
            }
            _syncCheckCoolDown = System.currentTimeMillis() + 1000;
            Log.v(TAG, "allowSync time: " + watch.finish());
        }
        return _allowSync;
    }

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "AuthTopicClient.onConnected");
            _authTopicClient.registerAuthState();
            AuthTopicClient.dispatchRequestCommand(WebTransactionService.this);
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
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();

                if (extras.containsKey(PARAM_KEY) && WebTransaction.keyExists(this,
                        extras.getString(PARAM_KEY))) {
                    return;
                }

                WebTransaction transaction = WebTransaction.put(this,
                        (Priority) extras.getSerializable(PARAM_PRIORITY),
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        extras.getBoolean(PARAM_IS_SYNC),
                        extras.getByteArray(PARAM_REQUEST),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));

                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Parcelable[] transforms = extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = (Bundle) transforms[i];
                        Transform.put(this, transaction.getId(), transform);
                    }
                }

                transaction.setState(WebTransaction.State.IDLE);
                transaction.save(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        _manager.wakeUp();
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

//            Log.v(TAG, "Trans Count: " + WebTransaction.count(context));

            WebTransaction trans = WebTransaction.getNext(context, _syncThread && allowSync(), _isAuthenticated);

            // if failed, then exit
            if (trans == null) {
                // Log.v(TAG, "skip no transaction");
                return false;
            }

            // debug if have key, output
            if (!misc.isEmptyOrNull(trans.getKey())) {
                Log.v(TAG, "Key: " + trans.getKey());
            }

            // at some point call the web service
            JsonObject request = trans.getRequest().copy();
            String handlerName = null;
            HttpResult result = null;
            try {
                // apply authentication if needed
                if (trans.useAuth()) {
                    OAuth auth = getAuth();
                    if (!_isAuthenticated) {
                        // Log.v(TAG, "skip no auth");
                        trans.requeue(context);
                        return false;
                    }

                    if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                        request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                    }
                    request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                    auth.applyToRequest(request);
                }
                Log.v(TAG, request.display());

                handlerName = trans.getHandlerName();

                if (!misc.isEmptyOrNull(handlerName)) {
                    WebTransactionHandler.Result wresult = WebTransactionHandler.startTransaction(context, handlerName, trans);
                    // TODO handle result?
/*
                    switch (wresult) {
                        case ERROR:
                            WebTransaction.delete(context, trans.getId());
                            Transform.deleteTransaction(context, trans.getId());
                            break;
                        case FINISH:
                            WebTransaction.delete(context, trans.getId());
                            Transform.deleteTransaction(context, trans.getId());
                            break;
                        case REQUEUE:
                            trans.requeue(context);
                            break;
                    }
*/
                }

                // perform request
                result = HttpJson.run(context, request);

                // debug output
                try {
                    Log.v(TAG, result.getResponseCode() + "");
                    Log.v(TAG, result.getResponseMessage());
                    // Log.v(TAG, result.getString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // check for invalid auth
                if (!result.isFile()
                        && result.getString().equals("You must provide a valid OAuth token to make a request")) {
                    Log.v(TAG, "Reauth");
                    _isAuthenticated = false;
                    AuthTopicClient.dispatchInvalidateCommand(context);
                    trans.requeue(context);
                    AuthTopicClient.dispatchRequestCommand(context);
                    return true;
                } else if (result.getResponseCode() == 400) {
                    // Bad request
                    // need to report this
                    // need to re-auth?
                    Thread.sleep(5000);
                    trans.requeue(context);
                    AuthTopicClient.dispatchRequestCommand(context);
                } else if (result.getResponseCode() == 401) {
                    // 401 usually means bad auth token
                    Log.v(TAG, "Reauth");
                    _isAuthenticated = false;
                    AuthTopicClient.dispatchInvalidateCommand(context);
                    trans.requeue(context);
                    AuthTopicClient.dispatchRequestCommand(context);
                    return true;
                } else if (result.getResponseCode() == 404) {
                    // not found?... error
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result);
                    WebTransaction.delete(context, trans.getId());
                    Transform.deleteTransaction(context, trans.getId());
                    return true;
                    // usually means code is being updated on the server
                } else if (result.getResponseCode() == 502) {
                    Thread.sleep(5000);
                    trans.requeue(context);
                    AuthTopicClient.dispatchRequestCommand(context);
                    return true;
                } else if (result.getResponseCode() / 100 != 2) {
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result);
                    WebTransaction.delete(context, trans.getId());
                    Transform.deleteTransaction(context, trans.getId());
                    return true;
                }

                GlobalTopicClient.dispatchNetworkConnected(context);

                if (!misc.isEmptyOrNull(handlerName)) {
                    WebTransactionHandler.Result wresult = WebTransactionHandler.completeTransaction(
                            context, handlerName, trans, result);

                    switch (wresult) {
                        case ERROR:
                            WebTransactionHandler.failTransaction(context, handlerName, trans, result);
                            WebTransaction.delete(context, trans.getId());
                            Transform.deleteTransaction(context, trans.getId());
                            break;
                        case FINISH:
                            WebTransaction.delete(context, trans.getId());
                            Transform.deleteTransaction(context, trans.getId());
                            break;
                        case REQUEUE:
                            trans.requeue(context);
                            break;
                    }
                }
            } catch (MalformedURLException ex) {
                if (handlerName != null && result != null)
                    WebTransactionHandler.failTransaction(context, handlerName, trans, result);
                WebTransaction.delete(context, trans.getId());
                Transform.deleteTransaction(context, trans.getId());
            } catch (UnknownHostException ex) {
                // probably offline
                GlobalTopicClient.dispatchNetworkDisconnected(context);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                ex.printStackTrace();
                trans.requeue(context);
            } catch (Exception ex) {
                // no freaking clue
                ex.printStackTrace();
                trans.requeue(context);
            }
            return true;
        }

    }
}

package com.fieldnation.service.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.ThreadManager;
import com.fieldnation.service.MSService;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;

import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * This class accepts web requests and stores them into the queue for processing
 */
public class WebTransactionService extends MSService implements WebTransactionConstants {
    private static final String TAG = "WebTransactionService";

    private static final Object AUTH_LOCK = new Object();

    private OAuth _auth;
    private AuthTopicClient _authTopicClient;
    private GlobalTopicClient _globalTopicClient;
    private ThreadManager _manager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        WebTransaction.saveOrphans();

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
        TransactionThread t = new TransactionThread(_manager, this, false);
        t._isFirstThread = true;
        _manager.addThread(t); // 0

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
        return WebTransaction.count() > 0;
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

    protected OAuth getAuth() {
        Log.v(TAG, "getAuth");
        synchronized (AUTH_LOCK) {
            return _auth;
        }
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
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
            _manager.wakeUp();
        }

        @Override
        public void onNotAuthenticated() {
            _auth = null;
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

                if (extras.containsKey(PARAM_KEY) && WebTransaction.keyExists(extras.getString(PARAM_KEY))) {
                    Log.v(TAG, "processIntent end duplicate " + extras.getString(PARAM_KEY));
                    _manager.wakeUp();
                    return;
                }

                Log.v(TAG, "processIntent building transaction");
                WebTransaction transaction = WebTransaction.put(
                        (Priority) extras.getSerializable(PARAM_PRIORITY),
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        extras.getBoolean(PARAM_IS_SYNC),
                        extras.getByteArray(PARAM_REQUEST),
                        extras.getBoolean(PARAM_WIFI_REQUIRED),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));

                Log.v(TAG, "processIntent building transforms");
                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Parcelable[] transforms = extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = (Bundle) transforms[i];
                        Transform.put(transaction.getId(), transform);
                    }
                }

                Log.v(TAG, "processIntent saving transaction");
                transaction.setState(WebTransaction.State.IDLE);
                transaction.save();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        _manager.wakeUp();

        Log.v(TAG, "processIntent end");
    }


    public boolean isAuthenticated() {
        return _auth != null;
    }
}

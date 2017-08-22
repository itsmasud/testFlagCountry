package com.fieldnation.service.transaction;

import android.content.Context;
import android.os.Handler;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.auth.OAuth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * This class accepts web requests and stores them into the queue for processing
 */
public class WebTransactionService implements WebTransactionConstants {
    private static final String TAG = "WebTransactionService";
    private static final Object AUTH_LOCK = new Object();
    private static final long IDLE_TIMEOUT = 30000;

    private OAuth _auth;
    private ThreadManager _manager;
    private Handler _shutdownChecker;
    private long _lastRequestTime = 0;
    private static WebTransactionService _instance = null;

    public static WebTransactionService getInstance() {
        if (_instance == null)
            _instance = new WebTransactionService();
        return _instance;
    }

    private WebTransactionService() {
        Log.v(TAG, "Constructor");

        WebTransaction.saveOrphans();

        int threadCount = 4;
        if (App.get().isLowMemDevice()) {
            threadCount = 4;
        } else {
            threadCount = 8;
        }

        _authClient.subAuthStateChange();
        AuthClient.requestCommand();

        _appMessagingClient.subNetworkConnect();

        _manager = new ThreadManager();
        TransactionThread t = new TransactionThread(_manager, this, false);
        t._isFirstThread = true;
        _manager.addThread(t); // 0

        //_manager.addThread(new TransactionThread(_manager, this, false)); // 0
        _manager.addThread(new TransactionThread(_manager, this, true)); // 1
        for (int i = 2; i < threadCount; i++) {
            // every other can do sync
            _manager.addThread(new TransactionThread(_manager, this, i % 2 == 1));
        }

        startActivityMonitor();
    }

    public boolean isStillWorking() {
        return WebTransaction.count() > 0;
    }

    private void startActivityMonitor() {
        if (_shutdownChecker == null) {
            _shutdownChecker = new Handler();
        }
        _shutdownChecker.postDelayed(_activityChecker_runnable, IDLE_TIMEOUT);
    }

    private final Runnable _activityChecker_runnable = new Runnable() {
        @Override
        public void run() {
            if (isStillWorking()) {
                startActivityMonitor();
            } else if (System.currentTimeMillis() - _lastRequestTime > IDLE_TIMEOUT) {
                // shutdown
                shutDown();
            } else {
                startActivityMonitor();
            }
        }
    };

    public void shutDown() {
        Log.v(TAG, "onDestroy");
        _authClient.unsubAuthStateChange();
        _appMessagingClient.unsubNetworkConnect();
        _manager.shutdown();
        _instance = null;
    }

    private void setAuth(OAuth auth) {
        Log.v(TAG, "setAuth start");
        synchronized (AUTH_LOCK) {
            _auth = auth;
        }
        if (_manager != null)
            _manager.wakeUp();
        Log.v(TAG, "setAuth end");
    }

    protected OAuth getAuth() {
        Log.v(TAG, "getAuth");
        synchronized (AUTH_LOCK) {
            return _auth;
        }
    }

    private final AppMessagingClient _appMessagingClient = new AppMessagingClient() {
        @Override
        public void onNetworkConnect() {
            _manager.wakeUp();
        }
    };

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "AuthTopicClient.onAuthenticated");
            setAuth(oauth);
            if (_manager != null)
                _manager.wakeUp();
        }

        @Override
        public void onNotAuthenticated() {
            _auth = null;
        }
    };

    public boolean isAuthenticated() {
        return _auth != null;
    }

    /*-*************************-*/
    /*-         Queue           -*/
    /*-*************************-*/
    private static Handler _mainHandler = null;
    private static List<WebTransaction> TRANSACTION_QUEUE = new LinkedList<>();

    private static Handler getHandler() {
        if (_mainHandler == null) {
            _mainHandler = new Handler(App.get().getMainLooper());
        }
        return _mainHandler;
    }

    public static void queueTransaction(Context context, WebTransaction transaction) {
        synchronized (TRANSACTION_QUEUE) {
            TRANSACTION_QUEUE.add(transaction);
        }

        // Put on main thread
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                processQueue();
            }
        });
    }

    private static void processQueue() {
        while (true) {
            WebTransaction webTransaction = null;
            synchronized (TRANSACTION_QUEUE) {
                if (TRANSACTION_QUEUE.size() > 0) {
                    webTransaction = TRANSACTION_QUEUE.remove(0);
                }
            }
            if (webTransaction == null)
                return;

            try {

                if (webTransaction.getKey() != null && WebTransaction.keyExists(webTransaction.getKey())) {
                    Log.v(TAG, "processIntent end duplicate " + webTransaction.getKey());
                    getInstance()._manager.wakeUp();
                    return;
                }
                //Log.v(TAG, "processIntent saving transaction");
                webTransaction.setState(WebTransaction.State.IDLE);
                webTransaction.save();

                String listenerName = webTransaction.getListenerName();
                if (!misc.isEmptyOrNull(listenerName)) {
                    WebTransactionDispatcher.queued(App.get(), listenerName, webTransaction);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            getInstance()._manager.wakeUp();
        }
    }
}



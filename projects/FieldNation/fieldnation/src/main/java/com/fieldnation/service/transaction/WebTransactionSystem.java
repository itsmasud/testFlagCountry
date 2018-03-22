package com.fieldnation.service.transaction;

import android.content.Context;
import android.os.Handler;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.auth.OAuth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * This class accepts web requests and stores them into the queue for processing
 */
public class WebTransactionSystem implements WebTransactionConstants {
    private static final String TAG = "WebTransactionSystem";
    private static final Object AUTH_LOCK = new Object();
    private static final long IDLE_TIMEOUT = 30000;

    private OAuth _auth;
    private ThreadManager _manager;
    private Handler _shutdownChecker;
    private static WebTransactionSystem _instance = null;
    private static Handler _mainHandler = null;

    private static Handler getHandler() {
        if (_mainHandler == null) {
            _mainHandler = new Handler(App.get().getMainLooper());
        }
        return _mainHandler;
    }

    public static WebTransactionSystem getInstance() {
        synchronized (TAG) {
            if (_instance == null)
                _instance = new WebTransactionSystem();
            return _instance;
        }
    }

    public static void stop() {
        if (_instance != null)
            _instance.shutDown();
    }

    private WebTransactionSystem() {
        Log.v(TAG, "Constructor");

        WebTransaction.saveOrphans();

        int threadCount = 4;
        if (App.get().isLowMemDevice()) {
            threadCount = 2;
        } else {
            threadCount = 4;
        }

        _authClient.subAuthStateChange();
        AuthClient.requestCommand();

        _appMessagingClient.subNetworkConnect();

        _manager = new ThreadManager();

        _manager.addThread(new QueueProcessingThread(_manager));

        TransactionThread t = new TransactionThread(_manager, this);
        t._isFirstThread = true;
        _manager.addThread(t); // 0

        for (int i = 1; i < threadCount; i++) {
            _manager.addThread(new TransactionThread(_manager, this));
        }

        startActivityMonitor();
    }

    public boolean isStillWorking() {
        boolean stillWorking = WebTransaction.count() > 0;
        synchronized (TRANSACTION_QUEUE) {
            stillWorking = stillWorking || TRANSACTION_QUEUE.size() > 0;
        }
        return stillWorking;
    }

    private void startActivityMonitor() {
        if (_shutdownChecker == null) {
            _shutdownChecker = new Handler(App.get().getMainLooper());
        }
        _shutdownChecker.postDelayed(_activityChecker_runnable, IDLE_TIMEOUT);
    }

    private final Runnable _activityChecker_runnable = new Runnable() {
        @Override
        public void run() {
            if (isStillWorking()) {
                startActivityMonitor();
            } else {
                shutDown();
            }
        }
    };

    public void shutDown() {
        Log.v(TAG, "shutDown");
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
        }

        @Override
        public void onNotAuthenticated() {
            _auth = null;
        }
    };

    protected boolean isAuthenticated() {
        return _auth != null;
    }

    /*-*************************-*/
    /*-         Queue           -*/
    /*-*************************-*/
    protected static final List<WebTransaction> TRANSACTION_QUEUE = new LinkedList<>();

    public static void queueTransaction(Context context, WebTransaction transaction) {
        if (transaction.getUUID() != null) {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(transaction.getUUID()))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.START, "Web Transaction Queue System"))
                    .build());
        }

        synchronized (TRANSACTION_QUEUE) {
            TRANSACTION_QUEUE.add(transaction);
        }
        getInstance()._manager.wakeUp();

        if (transaction.getUUID() != null) {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(transaction.getUUID()))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Web Transaction Queue System"))
                    .build());
        }
    }
}



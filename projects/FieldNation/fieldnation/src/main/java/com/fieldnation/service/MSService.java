package com.fieldnation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.ThreadManager;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a lot like an IntentService, however, it lets the extending class choose the number
 * of threads. Also manages the lifecycle of the service for you. Will shutdown a short time after
 * the service has been inactive.
 * <p/>
 * Created by Michael Carver on 4/24/2015.
 */
public abstract class MSService extends Service {
    private final String TAG;
    private static final long IDLE_TIMEOUT = 30000;

    private final Object LOCK = new Object();

    private ThreadManager _manager = new ThreadManager();
    private List<Intent> _intents = new LinkedList<>();

    private long _lastRequestTime = 0;
    private int _workersWorking = 0;

    private Handler _shutdownChecker;

    public MSService() {
        super();
        TAG = "MSService/" + this.getClass().getSimpleName();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        int maxWorkerCount = getMaxWorkerCount();

        for (int i = 0; i < maxWorkerCount; i++) {
            _manager.addThread(new WorkerThread(_manager, _intents));
        }

        startActivityMonitor();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract int getMaxWorkerCount();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand start");
        if (intent != null) {
            _lastRequestTime = System.currentTimeMillis();
            synchronized (LOCK) {
                addIntent(_intents, intent);

//                Log.v(TAG, "intents " + _intents.size());
            }
        }

        _manager.wakeUp();
        Log.v(TAG, "onStartCommand end");
        return START_STICKY;
    }

    public void addIntent(List<Intent> intents, Intent intent) {
        intents.add(intent);
    }

    /**
     * Periodically checks the status of the service to determine if it can be shutdown.
     */
    private void startActivityMonitor() {
        if (_shutdownChecker == null) {
            _shutdownChecker = new Handler();
        }
        _shutdownChecker.postDelayed(_activityCheck_runnable, IDLE_TIMEOUT);
    }

    private final Runnable _activityCheck_runnable = new Runnable() {
        @Override
        public void run() {
            // check timer
            if (isStillWorking()) {
                startActivityMonitor();
            } else if ((System.currentTimeMillis() - _lastRequestTime > IDLE_TIMEOUT
                    && _workersWorking == 0
                    && _intents.size() == 0)) {
                // shutdown
                stopSelf();
            } else {
                startActivityMonitor();
            }
        }
    };

    public boolean isStillWorking() {
        return false;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _manager.shutdown();
        super.onDestroy();
    }

    /**
     * Called by the worker thread. Perform any operations you need here
     *
     * @param intent
     */
    public abstract void processIntent(Intent intent);

    /**
     * A worker thread that runs through all the intents.
     */
    class WorkerThread extends ThreadManager.ManagedThread {
        private List<Intent> _intents;

        public WorkerThread(ThreadManager manager, List<Intent> intents) {
            super(manager);
            _intents = intents;
            start();
        }

        @Override
        public boolean doWork() {
            Intent intent = null;
            synchronized (LOCK) {
                if (_intents.size() > 0) {
                    intent = _intents.remove(0);
                }
                //Log.v(TAG, "intents " + _intents.size());
            }

            if (intent != null) {
                try {
                    _workersWorking++;
                    processIntent(intent);
                } finally {
                    _workersWorking--;
                }
                return true;
            }
            return false;
        }
    }
}

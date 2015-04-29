package com.fieldnation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.ThreadManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 4/24/2015.
 */
public abstract class MSService extends Service {
    private static final String TAG = "MSService";
    private final Object LOCK = new Object();

    private ThreadManager _manager = new ThreadManager();
    private List<Intent> _intents = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        int maxWorkerCount = getMaxWorkerCount();

        for (int i = 0; i < maxWorkerCount; i++) {
            _manager.addThread(getNewWorker(_manager, _intents));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract int getMaxWorkerCount();

    public abstract WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null) {
            synchronized (LOCK) {
                _intents.add(intent);

                Log.v(TAG, "intents " + _intents.size());
            }
        }

        _manager.wakeUp();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        _manager.shutdown();
        super.onDestroy();
    }

    public abstract class WorkerThread extends ThreadManager.ManagedThread {
        private List<Intent> _intents;

        public WorkerThread(ThreadManager manager, String name, List<Intent> intents) {
            super(manager, name);
            Log.v(TAG, "Starting Thread " + name);
            _intents = intents;

            start();
        }

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

                Log.v(TAG, "intents " + _intents.size());
            }

            if (intent != null) {
                processIntent(intent);
                return true;
            }
            return false;
        }

        public abstract void processIntent(Intent intent);
    }
}

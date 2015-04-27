package com.fieldnation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 4/24/2015.
 */
public abstract class MSService extends Service {
    private static final String TAG = "MSService";
    private final Object LOCK = new Object();

    private List<Intent> _intents = new LinkedList<>();
    private List<WorkerThread> _workers = new LinkedList<>();
    private List<WorkerThread> _deadWorkers = new LinkedList<>();
    private int _maxWorkerCount;

    @Override
    public void onCreate() {
        super.onCreate();
        _maxWorkerCount = getMaxWorkerCount();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract int getMaxWorkerCount();

    public abstract WorkerThread getNewWorker(List<Intent> intents);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null) {
            synchronized (LOCK) {
                _intents.add(intent);

                Log.v(TAG, "cleaning up dead workers");
                for (int i = 0; i < _deadWorkers.size(); i++) {
                    _workers.remove(_deadWorkers.get(i));
                }
                _deadWorkers.clear();

                Log.v(TAG, "Adding workers");
                while (_workers.size() < Math.min((_intents.size() / 2) + 1, _maxWorkerCount)) {
                    _workers.add(getNewWorker(_intents));
                }
            }

            Log.v(TAG, "onStartCommand r:" + _workers.size() + " i:" + _intents.size());
        }
        return START_STICKY;
    }

    private void workerFinished(WorkerThread thread) {
        synchronized (LOCK) {
            _deadWorkers.add(thread);
        }
    }

    @Override
    public void onDestroy() {
        synchronized (LOCK) {
            for (int i = 0; i < _deadWorkers.size(); i++) {
                _workers.remove(_deadWorkers.get(i));
            }
            _deadWorkers.clear();

            for (int i = 0; i < _workers.size(); i++) {
                _workers.get(i).shutdown();
            }
        }

        for (int i = 0; i < _workers.size(); i++) {
            WorkerThread worker = _workers.get(i);
            try {
                worker.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (worker.isAlive()) {
                worker.interrupt();
            }
        }

        _workers.clear();

        super.onDestroy();
    }

    public abstract class WorkerThread extends Thread {
        private List<Intent> _intents;
        private boolean _running = true;

        public WorkerThread(String name, List<Intent> intents) {
            super(name);
            Log.v(TAG, "Starting Thread " + name);
            _intents = intents;
            _running = true;
            start();
        }

        public void shutdown() {
            _running = false;
        }

        @Override
        public void run() {
            while (_running) {
                Intent intent = null;
                synchronized (LOCK) {
                    if (_intents.size() > 0) {
                        intent = _intents.remove(0);
                    }
                }

                if (intent == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                processIntent(intent);
            }
            workerFinished(this);
        }

        public abstract void processIntent(Intent intent);
    }
}

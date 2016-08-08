package com.fieldnation;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 8/6/2016.
 */
public class ThreadPool {
    private static final String TAG = "ThreadPool";
    private final Object THREAD_PAUSE = new Object();

    private final List<ManagedThread> _threads = new LinkedList<>();
    private WorkHandler _listener;

    public ThreadPool(int count, WorkHandler listener) {
        Log.v(TAG, "ThreadPool");
        _listener = listener;

        for (int i = 0; i < count; i++) {
            _threads.add(new ManagedThread(this));
        }
    }

    public void shutdown() {
        Log.v(TAG, "shutdown start");
        for (int i = 0; i < _threads.size(); i++) {
            _threads.get(i).finish();
        }

        wakeUp();

        for (int i = 0; i < _threads.size(); i++) {
            ManagedThread thread = _threads.get(i);
            try {
                thread.join(100);
            } catch (InterruptedException e) {
                Log.v(TAG, e);
            }

            if (thread.isAlive())
                thread.interrupt();
        }

        Log.v(TAG, "shutdown end");
    }

    public void wakeUp() {
        synchronized (THREAD_PAUSE) {
            THREAD_PAUSE.notifyAll();
        }
    }

    private static class ManagedThread extends Thread {
        private boolean _running = true;
        private final Object THREAD_PAUSE;
        private final WorkHandler _listener;

        public ManagedThread(ThreadPool manager) {
            super();
            THREAD_PAUSE = manager.THREAD_PAUSE;
            _listener = manager._listener;
            setName("ManagedThread/" + getClass().getSimpleName());
            start();
        }

        void finish() {
            _running = false;
        }

        private void sleep() {
            synchronized (THREAD_PAUSE) {
                try {
                    THREAD_PAUSE.wait(1000);
                } catch (InterruptedException e) {
                    Log.v(TAG, e);
                }
            }
        }

        @Override
        public void run() {
            while (_running) {
                if (_listener == null || !_listener.doWork())
                    sleep();
            }
        }
    }

    public interface WorkHandler {
        /**
         * Called when a worker thread is ready to process a step
         *
         * @return true if work was done, false if work was not done.
         */
        boolean doWork();
    }
}

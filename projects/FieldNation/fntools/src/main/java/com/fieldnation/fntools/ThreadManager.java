package com.fieldnation.fntools;

import com.fieldnation.fnlog.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 4/28/2015.
 */
public class ThreadManager {
    private static final String TAG = "ThreadManager";
    private final Object THREAD_PAUSE = new Object();

    private final List<ManagedThread> _threads = new LinkedList<>();

    public ThreadManager() {
        Log.v(TAG, "ThreadManager");
    }

    public void addThread(ManagedThread thread) {
        _threads.add(thread);
    }

    public void shutdown() {
        Log.v(TAG, "shutdown start");
        for (int i = 0; i < _threads.size(); i++) {
            _threads.get(i).finish();
        }

        wakeUp();

/*
        for (int i = 0; i < _threads.size(); i++) {
            ManagedThread thread = _threads.get(i);
            try {
                thread.join();
            } catch (InterruptedException e) {
                Log.v(TAG, e);
            }

            if (thread.isAlive())
                thread.interrupt();
        }
*/

        Log.v(TAG, "shutdown end");
    }

    public void wakeUp() {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                synchronized (THREAD_PAUSE) {
                    THREAD_PAUSE.notifyAll();
                }
                return null;
            }
        }.executeEx();
    }

    public static abstract class ManagedThread extends Thread {
        private boolean _running = true;
        private final Object THREAD_PAUSE;

        public ManagedThread(ThreadManager manager) {
            super();
            THREAD_PAUSE = manager.THREAD_PAUSE;
            setName("ManagedThread/" + getClass().getSimpleName());
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
                if (!doWork()) {
                    sleep();
                }
            }
        }

        /**
         * @return true if work was done, false if work was not done.
         */
        public abstract boolean doWork();
    }
}

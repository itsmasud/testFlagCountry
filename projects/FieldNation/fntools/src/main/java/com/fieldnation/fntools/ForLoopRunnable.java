package com.fieldnation.fntools;


import android.os.Handler;

import com.fieldnation.fnlog.Log;

/**
 * Created by Michael Carver on 1/5/2015.
 * This is a simple runnable that mimics a for loop, only it uses a {@Link Handler} to do the
 * stepping. Useful for building large lists in the UI
 */
public abstract class ForLoopRunnable implements Runnable {
    private static final String TAG = "ForLoopRunnable";

    private int i;
    private final int count;
    private final long delay;
    private final Handler handler;
    private boolean _run = true;

    public ForLoopRunnable(int count, Handler handler, long delay) {
        this.count = count;
        i = 0;
        this.handler = handler;
        this.delay = delay;
    }

    public ForLoopRunnable(int count, Handler handler) {
        this(count, handler, 100);
    }

    public boolean isRunning() {
        return _run;
    }

    public void cancel() {
        _run = false;
    }

    @Override
    public void run() {
        if (!_run) {
            return;
        }
        try {
            if (i < count) {
                next(i);
            } else {
                finish(count);
                _run = false;
                return;
            }
            i++;
            handler.postDelayed(this, delay);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public abstract void next(int i) throws Exception;

    public void finish(int count) throws Exception {
    }
}

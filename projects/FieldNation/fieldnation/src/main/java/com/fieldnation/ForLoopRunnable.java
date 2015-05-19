package com.fieldnation;


import android.os.Handler;

/**
 * Created by Michael Carver on 1/5/2015.
 * This is a simple runnable that mimics a for loop, only it uses a {@Link Handler} to do the
 * stepping. Useful for building large lists in the UI
 */
public abstract class ForLoopRunnable implements Runnable {
    private int i;
    private final int count;
    private final long delay;
    private final Handler handler;

    public ForLoopRunnable(int count, Handler handler, long delay) {
        this.count = count;
        i = 0;
        this.handler = handler;
        this.delay = delay;
    }

    public ForLoopRunnable(int count, Handler handler) {
        this(count, handler, 50);
    }

    @Override
    public void run() {
        try {
            if (i < count) {
                next(i);
            } else {
                finish(count);
                return;
            }
            i++;
            handler.postDelayed(this, delay);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void next(int i) throws Exception;

    public void finish(int count) throws Exception {
    }
}

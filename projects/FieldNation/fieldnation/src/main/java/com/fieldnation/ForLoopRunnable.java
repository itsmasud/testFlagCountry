package com.fieldnation;


import android.os.Handler;

/**
 * Created by Michael Carver on 1/5/2015.
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

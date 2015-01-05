package com.fieldnation;

/**
 * Created by Michael Carver on 1/5/2015.
 */
public abstract class ForLoopRunnable implements Runnable {
    private int i;
    private int count;

    public ForLoopRunnable(int count) {
        this.count = count;
        i = 0;
    }

    @Override
    public void run() {
        if (i < count) {
            next(i);
        }
        i++;
    }

    public abstract void next(int i);
}

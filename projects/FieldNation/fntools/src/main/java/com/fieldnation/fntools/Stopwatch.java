package com.fieldnation.fntools;

public class Stopwatch {
    private boolean running;
    private long start_time;
    private long pause_time = 0;
    private boolean paused;

    public Stopwatch() {
        start_time = System.currentTimeMillis();
        running = true;
        paused = false;
    }

    public Stopwatch(boolean start) {
        if (start) {
            start_time = System.currentTimeMillis();
            running = true;
        } else {
            running = false;
        }
        paused = false;
    }

    public void start() {
        if (!running) {
            start_time = System.currentTimeMillis();
            running = true;
        } else if (paused) {
            unpause();
        }
    }

    public void pause() {
        if (running) {
            pause_time = System.currentTimeMillis();
            paused = true;
        }
    }

    public void unpause() {
        if (running && paused) {
            start_time = System.currentTimeMillis() - (pause_time - start_time);
            pause_time = 0;
            paused = false;
        }
    }

    public long getTime() {
        if (paused) {
            return pause_time - start_time;
        } else {
            return System.currentTimeMillis() - start_time;
        }
    }

    public long finish() {
        if (running) {
            running = false;
            return getTime();
        }
        return 0;
    }

    public long finishAndRestart() {
        long t = finish();
        start();
        return t;
    }
}

package com.fieldnation.fnpigeon;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/25/17.
 */
class MessageRunnable implements Runnable {
    private Pigeon pigeon;
    private Object message;
    private String address;

    private MessageRunnable(Pigeon pigeon, String address, Object message) {
        this.pigeon = pigeon;
        this.message = message;
        this.address = address;
    }

    @Override
    public void run() {
        pigeon.onMessage(address, message);
        pigeon = null;
        message = null;
        address = null;
        synchronized (POOL) {
            POOL.add(this);
        }
    }

    private static final List<MessageRunnable> POOL = new LinkedList<>();

    public static MessageRunnable getInstance(Pigeon pigeon, String address, Object message) {
        synchronized (POOL) {
            if (POOL.size() > 0) {
                MessageRunnable mr = POOL.remove(0);
                mr.pigeon = pigeon;
                mr.address = address;
                mr.message = message;
                return mr;
            } else {
                return new MessageRunnable(pigeon, address, message);
            }
        }
    }
}

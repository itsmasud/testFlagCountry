package com.fieldnation.fnpigeon;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/25/17.
 */
class MessagesRunnable implements Runnable {
    private List<Pigeon> pigeonList;
    private List<Object> objectList;
    private List<String> addressList;

    private MessagesRunnable(List<Pigeon> pigeonList, List<String> addressList, List<Object> objectList) {
        this.pigeonList = pigeonList;
        this.objectList = objectList;
        this.addressList = addressList;
    }

    @Override
    public void run() {
        while (pigeonList.size() > 0) {
            pigeonList.remove(0).onMessage(addressList.remove(0), objectList.remove(0));
        }
        pigeonList = null;
        objectList = null;
        addressList = null;
        synchronized (POOL) {
            POOL.add(this);
        }
    }

    private static final List<MessagesRunnable> POOL = new LinkedList<>();

    public static MessagesRunnable getInstance(List<Pigeon> pigeonList, List<String> addressList, List<Object> objectList) {
        synchronized (POOL) {
            if (POOL.size() > 0) {
                MessagesRunnable mr = POOL.remove(0);
                mr.pigeonList = pigeonList;
                mr.objectList = objectList;
                mr.addressList = addressList;
                return mr;
            } else {
                return new MessagesRunnable(pigeonList, addressList, objectList);
            }
        }
    }
}

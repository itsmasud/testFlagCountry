package com.fieldnation.v2.data.client;

import java.util.Hashtable;
import java.util.Set;

/**
 * Created by mc on 8/10/17.
 */

public class MemoryCache {
    private static final String TAG = "MemoryCache";
    private static Hashtable<Integer, Node> CACHE = new Hashtable<>();
    private static int CACHE_NEXT = 0;

    private static class Node {
        long ttl;
        Object object;

        public Node(Object object) {
            this.object = object;
            ttl = System.currentTimeMillis() + 30000;
        }
    }

    public static int put(Object object) {
        synchronized (TAG) {
            int id = CACHE_NEXT;
            CACHE.put(id, new Node(object));
            CACHE_NEXT++;
            return id;
        }
    }

    public static Object get(int key) {
        synchronized (TAG) {
            Object value = CACHE.get(key).object;
            purgeNodes();
            return value;
        }
    }

    public static void purgeNodes() {
        synchronized (TAG) {
            Set<Integer> keySet = CACHE.keySet();
            Integer[] keys = CACHE.keySet().toArray(new Integer[keySet.size()]);
            for (Integer key : keys) {
                Node node = CACHE.get(key);
                if (node.ttl < System.currentTimeMillis()) {
                    CACHE.remove(key);
                }
            }
        }
    }

}

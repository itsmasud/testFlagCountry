package com.fieldnation.topics;

import android.os.ResultReceiver;

import java.util.Hashtable;

/**
 * Created by michael.carver on 12/12/2014.
 */
public class TopicClient {
    public int resultCode;
    public ResultReceiver receiver;
    public int id;
    public String topicId;
    public boolean isValid = true;

    private static Integer _counter = 0;
    private static Hashtable<Integer, TopicClient> _instances = new Hashtable<>();

    public TopicClient() {
        synchronized (_counter) {
            _counter++;
            id = _counter;
            _instances.put(id, this);
        }
    }

    public static TopicClient remove(int uid) {
        _instances.get(uid).isValid = false;
        return _instances.remove(uid);
    }

    public static void clearAll() {
        _instances.clear();
        _instances = null;
        _counter = 0;
    }
}

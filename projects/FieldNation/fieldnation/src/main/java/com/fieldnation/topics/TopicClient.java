package com.fieldnation.topics;

import android.os.ResultReceiver;

import com.fieldnation.Log;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by michael.carver on 12/12/2014.
 */
class TopicClient {
    private static final String TAG = "TopicClient";

    public int resultCode;
    public ResultReceiver receiver;
    public String tag;
    public Set<String> topics = new HashSet<>();

    private static Hashtable<String, TopicClient> _instances = new Hashtable<>();
    private static Hashtable<String, Set<TopicClient>> _topics = new Hashtable<>();

    static {
        Log.v(TAG, "TOPIC_CLIENT_RESET!");
    }

    public TopicClient(String tag) {
        this.tag = tag;
    }

    public static void reset() {
        _instances = new Hashtable<>();
        _topics = new Hashtable<>();
    }

    public static Set<TopicClient> getSet(String topic) {
        if (!_topics.containsKey(topic)) {
            _topics.put(topic, new HashSet<TopicClient>());
        }

        return _topics.get(topic);
    }


    public static TopicClient get(String tag) {
        if (!_instances.containsKey(tag)) {
            _instances.put(tag, new TopicClient(tag));
        }

        return _instances.get(tag);
    }

    public static void delete(String tag) {
        TopicClient c = _instances.get(tag);

        Enumeration<String> e = _topics.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            Set<TopicClient> clients = getSet(key);

            if (clients.contains(c))
                clients.remove(c);
        }

        _instances.remove(tag);
    }

    public static void unregister(String tag, String topic) {
        TopicClient c = get(tag);
        c.topics.remove(topic);

        Set<TopicClient> clients = getSet(topic);
        clients.remove(c);
    }


    public void addTopic(String topic) {
        topics.add(topic);
        if (getSet(topic).add(this)) {
            Log.v(TAG, "Added " + tag + " to  " + topic);
        } else {
            Log.v(TAG, "Add Fail " + tag + " to  " + topic);
        }
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}

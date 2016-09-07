package com.fieldnation.service.topics;

import android.os.Messenger;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Michael Carver on 3/3/2015.
 */
class TopicUser {
    private static final String TAG = "TopicUser";

    /**
     * The messenger to use to send events to the user
     */
    public Messenger messenger;
    /**
     * A tag that identifies the user
     */
    public final String userTag;
    /**
     * The topics that this user is listening to
     */
    public final Set<String> topics = new HashSet<>();

    /**
     * All of the registered users indexed by their userTag
     */
    private static Hashtable<String, TopicUser> _instances = new Hashtable<>();
    /**
     * All of the users that are listening to a topic. Indexed by topic
     */
    private static Hashtable<String, Set<TopicUser>> _topics = new Hashtable<>();

    public TopicUser(String userTag) {
        this.userTag = userTag;
    }

    public static void reset() {
        _instances = new Hashtable<>();
        _topics = new Hashtable<>();
    }

    public static Set<TopicUser> getUsers(String topic) {
        if (!_topics.containsKey(topic)) {
            _topics.put(topic, new HashSet<TopicUser>());
        }

        return _topics.get(topic);
    }


    public static TopicUser getUser(String userTag) {
        if (!_instances.containsKey(userTag)) {
            _instances.put(userTag, new TopicUser(userTag));
        }

        return _instances.get(userTag);
    }

    public static void deleteUser(String userTag) {
        TopicUser c = _instances.get(userTag);

        Set<String> e = _topics.keySet();
        for (String key : e) {
            Set<TopicUser> clients = getUsers(key);
            if (clients.contains(c))
                clients.remove(c);
        }

        _instances.remove(userTag);
    }

    public static void unregisterUser(String userTag, String topic) {
        TopicUser c = getUser(userTag);
        c.topics.remove(topic);

        Set<TopicUser> clients = getUsers(topic);
        clients.remove(c);
    }


    public void addTopic(String topic) {
        topics.add(topic);
        if (getUsers(topic).add(this)) {
//            Log.v(TAG, "Added " + userTag + " to " + topic);
        } else {
//            Log.v(TAG, "Add Fail " + userTag + " to " + topic);
        }
    }

    @Override
    public int hashCode() {
        return userTag.hashCode();
    }

}

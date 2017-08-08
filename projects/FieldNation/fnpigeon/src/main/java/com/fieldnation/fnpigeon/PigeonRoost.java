package com.fieldnation.fnpigeon;

import android.os.Parcelable;

import com.fieldnation.fnlog.Log;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by mc on 8/8/17.
 */

public class PigeonRoost implements TopicConstants {
    public static final String TAG = "Manager";

    private static Hashtable<String, StickyContainer> _stickies = new Hashtable<>();
    private static Hashtable<Pigeon, Set<String>> _topicsByListener = new Hashtable<>();
    private static Hashtable<String, Set<Pigeon>> _listenersByTopic = new Hashtable<>();

    private static Set<Pigeon> getListenersByTopic(String topicId) {
        synchronized (TAG) {
            if (!_listenersByTopic.containsKey(topicId)) {
                _listenersByTopic.put(topicId, new HashSet<Pigeon>());
            }
            return _listenersByTopic.get(topicId);
        }
    }

    private static Set<String> getTopicsByListener(Pigeon listener) {
        synchronized (TAG) {
            if (!_topicsByListener.containsKey(listener)) {
                _topicsByListener.put(listener, new HashSet<String>());
            }
            return _topicsByListener.get(listener);
        }
    }

    public static void register(Pigeon listener, String topicId) {
        synchronized (TAG) {
            // Add to the tables
            getListenersByTopic(topicId).add(listener);
            Set<String> topics = getTopicsByListener(listener);
            topics.add(topicId);

            if (_stickies.containsKey(topicId)) {
                StickyContainer stickyContainer = _stickies.get(topicId);
                listener.onTopic(topicId, stickyContainer.parcel);
            }
        }
    }

    public static void unregister(Pigeon listener, String topicId) {
        synchronized (TAG) {
            if (_listenersByTopic.containsKey(topicId)) {
                _listenersByTopic.get(topicId).remove(listener);
            }

            if (_topicsByListener.containsKey(listener)) {
                _topicsByListener.get(listener).remove(topicId);
            }
        }
    }

    public static void unregister(Pigeon listener) {
        if (_topicsByListener.containsKey(listener)) {
            Set<String> topics = _topicsByListener.get(listener);

            for (String topic : topics) {
                _listenersByTopic.get(topic).remove(listener);
            }

            _topicsByListener.remove(listener);
        }
    }

    public static void dispatchEvent(String topicId, Parcelable payload, Sticky sticky) {
        String[] topicIdTree = topicId.split("/");
        synchronized (TAG) {
            String currentTopic = topicIdTree[0];

            if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                _stickies.put(currentTopic, new StickyContainer(payload, sticky));
            }
            for (int i = 1; i < topicIdTree.length; i++) {
                currentTopic += "/" + topicIdTree[i];
                if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                    _stickies.put(currentTopic, new StickyContainer(payload, sticky));
                }
            }

            currentTopic = topicIdTree[0];
            if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                _stickies.put(currentTopic, new StickyContainer(payload, sticky));
            }

            Set<Pigeon> listeners = getListenersByTopic(currentTopic);
            for (Pigeon listener : listeners) {
                listener.onTopic(currentTopic, payload);
            }

            for (int i = 1; i < topicIdTree.length; i++) {
                currentTopic += "/" + topicIdTree[i];

                if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                    _stickies.put(currentTopic, new StickyContainer(payload, sticky));
                }
                listeners = getListenersByTopic(currentTopic);
                for (Pigeon listener : listeners) {
                    listener.onTopic(currentTopic, payload);
                }
            }
        }

        pruneStickies();
    }

    public static void clearTopic(String topicId) {
        synchronized (TAG) {
            if (_stickies.containsKey(topicId))
                _stickies.remove(topicId);
        }
    }

    public static void clearTopicAll(String topicId) {
        String[] topicIdTree = topicId.split("/");

        synchronized (TAG) {
            topicId = topicIdTree[0];
            if (_stickies.containsKey(topicId))
                _stickies.remove(topicId);

            for (int i = 1; i < topicIdTree.length; i++) {
                topicId += "/" + topicIdTree[i];
                if (_stickies.containsKey(topicId))
                    _stickies.remove(topicId);
            }
        }
    }

    public static void pruneStickies() {
        Hashtable<String, StickyContainer> ns = new Hashtable<>();
        synchronized (TAG) {
            Set<String> keys = _stickies.keySet();
            for (String key : keys) {
                StickyContainer sc = _stickies.get(key);

                if (sc.stickyType == Sticky.FOREVER) {
                    ns.put(key, sc);
                } else if (sc.stickyType == Sticky.TEMP
                        && sc.createdDate + 60000 > System.currentTimeMillis()) {
                    ns.put(key, sc);
                }
            }
            Log.v(TAG, "Pruning stickies done: " + ns.size() + "/" + _stickies.size());
            _stickies = ns;
        }
    }

    private static class StickyContainer {
        public Parcelable parcel;
        public long createdDate;
        public Sticky stickyType;

        public StickyContainer(Parcelable parcel, Sticky stickyType) {
            createdDate = System.currentTimeMillis();
            this.stickyType = stickyType;
            this.parcel = parcel;
        }
    }


}

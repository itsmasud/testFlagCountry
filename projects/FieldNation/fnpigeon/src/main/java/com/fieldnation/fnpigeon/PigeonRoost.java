package com.fieldnation.fnpigeon;

import android.os.Parcelable;

import com.fieldnation.fnlog.Log;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by mc on 8/8/17.
 */

public class PigeonRoost {
    public static final String TAG = "Manager";

    private static Hashtable<String, StickyContainer> _stickies = new Hashtable<>();
    private static Hashtable<Pigeon, Set<String>> _addressByPigeon = new Hashtable<>();
    private static Hashtable<String, Set<Pigeon>> _pigeonByAddress = new Hashtable<>();

    private static Set<Pigeon> getPigeonByTopic(String address) {
        synchronized (TAG) {
            if (!_pigeonByAddress.containsKey(address)) {
                _pigeonByAddress.put(address, new HashSet<Pigeon>());
            }
            return _pigeonByAddress.get(address);
        }
    }

    private static Set<String> getTopicsByPigeon(Pigeon pigeon) {
        synchronized (TAG) {
            if (!_addressByPigeon.containsKey(pigeon)) {
                _addressByPigeon.put(pigeon, new HashSet<String>());
            }
            return _addressByPigeon.get(pigeon);
        }
    }

    public static void sub(Pigeon pigeon, String address) {
        synchronized (TAG) {
            // Add to the tables
            getPigeonByTopic(address).add(pigeon);
            Set<String> addresses = getTopicsByPigeon(pigeon);
            addresses.add(address);

            if (_stickies.containsKey(address)) {
                StickyContainer stickyContainer = _stickies.get(address);
                pigeon.onMessage(address, stickyContainer.message);
            }
        }
    }

    public static void unsub(Pigeon pigeon, String address) {
        synchronized (TAG) {
            if (_pigeonByAddress.containsKey(address)) {
                _pigeonByAddress.get(address).remove(pigeon);
            }

            if (_addressByPigeon.containsKey(pigeon)) {
                _addressByPigeon.get(pigeon).remove(address);
            }
        }
    }

    public static void unsub(Pigeon pigeon) {
        if (_addressByPigeon.containsKey(pigeon)) {
            Set<String> topics = _addressByPigeon.get(pigeon);

            for (String topic : topics) {
                _pigeonByAddress.get(topic).remove(pigeon);
            }

            _addressByPigeon.remove(pigeon);
        }
    }

    public static void sendMessage(String address, Parcelable message, Sticky sticky) {
        String[] addressTree = address.split("/");
        synchronized (TAG) {
            // Fill stickies
            String currentAddress = addressTree[0];
            if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                _stickies.put(currentAddress, new StickyContainer(message, sticky));
            }

            for (int i = 1; i < addressTree.length; i++) {
                currentAddress += "/" + addressTree[i];
                if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                    _stickies.put(currentAddress, new StickyContainer(message, sticky));
                }
            }

            currentAddress = addressTree[0];
            if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                _stickies.put(currentAddress, new StickyContainer(message, sticky));
            }

            // Send message
            Set<Pigeon> pigeons = getPigeonByTopic(currentAddress);
            for (Pigeon pigeon : pigeons) {
                pigeon.onMessage(currentAddress, message);
            }

            for (int i = 1; i < addressTree.length; i++) {
                currentAddress += "/" + addressTree[i];

                if (sticky == Sticky.FOREVER || sticky == Sticky.TEMP) {
                    _stickies.put(currentAddress, new StickyContainer(message, sticky));
                }
                pigeons = getPigeonByTopic(currentAddress);
                for (Pigeon pigeon : pigeons) {
                    pigeon.onMessage(currentAddress, message);
                }
            }
        }

        pruneStickies();
    }

    public static void clearAddressCache(String address) {
        synchronized (TAG) {
            if (_stickies.containsKey(address))
                _stickies.remove(address);
        }
    }

    public static void clearAddressCacheAll(String address) {
        String[] addressTree = address.split("/");

        synchronized (TAG) {
            address = addressTree[0];
            if (_stickies.containsKey(address))
                _stickies.remove(address);

            for (int i = 1; i < addressTree.length; i++) {
                address += "/" + addressTree[i];
                if (_stickies.containsKey(address))
                    _stickies.remove(address);
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
        public Parcelable message;
        public long createdDate;
        public Sticky stickyType;

        public StickyContainer(Parcelable message, Sticky stickyType) {
            createdDate = System.currentTimeMillis();
            this.stickyType = stickyType;
            this.message = message;
        }
    }
}

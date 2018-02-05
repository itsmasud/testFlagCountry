package com.fieldnation.fnpigeon;

import android.os.Handler;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.Stopwatch;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mc on 8/8/17.
 */

public class PigeonRoost {
    public static final String TAG = "PigeonRoost";

    private static Handler mainHandler = null;
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

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(ContextProvider.get().getMainLooper());
        }
        return mainHandler;
    }

    /**
     * Subscribes a pigeon to the address
     *
     * @param pigeon
     * @param address
     */
    public static void sub(Pigeon pigeon, String address) {
        synchronized (TAG) {
            // Add to the tables
            getPigeonByTopic(address).add(pigeon);
            Set<String> addresses = getTopicsByPigeon(pigeon);
            addresses.add(address);

            if (_stickies.containsKey(address)) {
                StickyContainer stickyContainer = _stickies.get(address);
                getMainHandler().post(MessageRunnable.getInstance(pigeon, address, stickyContainer.message));
            }
        }
    }

    /**
     * Unsubscripes a specific pigeon from a specific address
     *
     * @param pigeon
     * @param address
     */
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

    /**
     * Unsubscribes a specific pigeon from all addresses
     *
     * @param pigeon
     */
    public static void unsub(Pigeon pigeon) {
        synchronized (TAG) {
            if (_addressByPigeon.containsKey(pigeon)) {
                Set<String> topics = _addressByPigeon.get(pigeon);

                for (String topic : topics) {
                    _pigeonByAddress.get(topic).remove(pigeon);
                }

                _addressByPigeon.remove(pigeon);
            }
        }
    }

    /**
     * Sends a message to all pigeons subscribed to the given address
     *
     * @param address
     * @param message
     * @param sticky
     */
    public static void sendMessage(String address, Object message, Sticky sticky) {
        Log.v(TAG, "sendMessage " + address);
        List<Pigeon> pigeonList = new LinkedList<>();
        List<Object> objectList = new LinkedList<>();
        List<String> addressList = new LinkedList<>();
        synchronized (TAG) {
            String[] addressTree = address.split("/");
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
            // Send message
            Set<Pigeon> pigeons = getPigeonByTopic(currentAddress);
            for (Pigeon pigeon : pigeons) {
                pigeonList.add(pigeon);
                objectList.add(message);
                addressList.add(currentAddress);
            }

            for (int i = 1; i < addressTree.length; i++) {
                currentAddress += "/" + addressTree[i];

                pigeons = getPigeonByTopic(currentAddress);
                for (Pigeon pigeon : pigeons) {
                    pigeonList.add(pigeon);
                    objectList.add(message);
                    addressList.add(currentAddress);
                }
            }
        }
        getMainHandler().post(MessagesRunnable.getInstance(pigeonList, addressList, objectList));
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
            Log.v(TAG, "Pruning stickies done: " + (_stickies.size() - ns.size()) + "/" + _stickies.size());
            _stickies = ns;
        }
    }
}

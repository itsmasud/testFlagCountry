package com.fieldnation.ui;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by mc on 1/16/17.
 */

public abstract class KeyedDispatcher<T> {
    private Hashtable<String, Set<T>> LISTENERS = new Hashtable<>();

    private Set<T> getSet(String key) {
        Set<T> set = null;
        if (LISTENERS.containsKey(key)) {
            set = LISTENERS.get(key);
        } else {
            set = new HashSet<>();
            LISTENERS.put(key, set);
        }
        return set;
    }

    public void add(String key, T listener) {
        getSet(key).add(listener);
    }

    public void remove(String key, T listener) {
        getSet(key).remove(listener);

        if (getSet(key).size() == 0)
            LISTENERS.remove(key);
    }

    public void removeAll(String key) {
        if (LISTENERS.containsKey(key))
            LISTENERS.remove(key);
    }


    public void dispatchAll(Object... parameters) {
        for (Set<T> set : LISTENERS.values()) {
            for (T listener : set) {
                try {
                    onDispatch(listener, parameters);
                } catch (Exception ex) {
                }
            }
        }
    }

    public void dispatch(String key, Object... parameters) {
        if (!LISTENERS.containsKey(key))
            return;

        Set<T> set = getSet(key);
        for (T listener : set) {
            try {
                onDispatch(listener, parameters);
            } catch (Exception ex) {
            }
        }
    }

    public abstract void onDispatch(T listener, Object... parameters);
}

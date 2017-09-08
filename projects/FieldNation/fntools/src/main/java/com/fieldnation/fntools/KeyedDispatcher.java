package com.fieldnation.fntools;

import android.os.Handler;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by mc on 1/16/17.
 */

public abstract class KeyedDispatcher<T> {
    private static final String TAG = "KeyedDispatcher";
    private Hashtable<String, Set<T>> LISTENERS = new Hashtable<>();

    private static Handler _handler = null;

    private static Handler getHandler() {
        if (_handler == null)
            _handler = new Handler(ContextProvider.get().getMainLooper());

        return _handler;
    }

    private Set<T> getSet(String key) {
        if (misc.isEmptyOrNull(key))
            return null;

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
        if (misc.isEmptyOrNull(key))
            return;

        getSet(key).add(listener);
    }

    public void remove(String key, T listener) {
        if (misc.isEmptyOrNull(key))
            return;

        getSet(key).remove(listener);

        if (getSet(key).size() == 0)
            LISTENERS.remove(key);
    }

    public void removeAll(String key) {
        if (misc.isEmptyOrNull(key))
            return;

        if (LISTENERS.containsKey(key))
            LISTENERS.remove(key);
    }


    public void dispatchAll(Object... parameters) {
        for (Set<T> set : LISTENERS.values()) {
            for (T listener : set) {
                getHandler().post(new DispatchRunnable<>(this, listener, parameters));
            }
        }
    }

    public void dispatch(String key, Object... parameters) {
        if (misc.isEmptyOrNull(key))
            return;

        if (!LISTENERS.containsKey(key))
            return;

        Set<T> set = getSet(key);
        for (T listener : set) {
            getHandler().post(new DispatchRunnable<>(this, listener, parameters));
        }
    }

    private static class DispatchRunnable<T> implements Runnable {
        KeyedDispatcher<T> dispatcher;
        T listener;
        Object[] parameters;

        DispatchRunnable(KeyedDispatcher<T> dispatcher, T listener, Object... parameters) {
            this.listener = listener;
            this.parameters = parameters;
            this.dispatcher = dispatcher;
        }

        @Override
        public void run() {
            dispatcher.onDispatch(listener, parameters);
        }
    }

    public abstract void onDispatch(T listener, Object... parameters);
}

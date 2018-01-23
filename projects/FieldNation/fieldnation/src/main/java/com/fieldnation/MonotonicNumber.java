package com.fieldnation;

import android.content.SharedPreferences;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.Stopwatch;

/**
 * Created by mc on 1/23/18.
 */

public class MonotonicNumber {
    private static final String TAG = "MonotonicNumber";

    private static final String PREF = "MonotonicNumber";

    private static boolean _hasRead = false;
    private static int _last = 0;


    public static synchronized int next() {
        if (!_hasRead) {
            _last = get();
            _hasRead = true;
        }

        _last++;

        put(_last);
        return _last;
    }

    private static int get() {
        SharedPreferences settings = App.get().getSharedPreferences(PREF, 0);
        return settings.getInt("number", 0);
    }

    private static void put(int number) {
        Log.v(TAG, "save " + number);
        Stopwatch stopwatch = new Stopwatch(true);
        SharedPreferences settings = App.get().getSharedPreferences(PREF, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("number", number);
        edit.apply();
        Log.v(TAG, "Write Time: " + stopwatch.finish());
    }
}

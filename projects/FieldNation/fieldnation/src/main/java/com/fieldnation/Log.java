package com.fieldnation;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }
}

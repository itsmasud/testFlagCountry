package com.fieldnation;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(tag, msg);
        Debug.log(android.util.Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, Throwable th) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(tag, th.getClass().getCanonicalName(), th);
        Debug.log(android.util.Log.VERBOSE, tag,
                th.getMessage() + "\n" +
                        android.util.Log.getStackTraceString(th));
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(tag, msg);
        Debug.log(android.util.Log.WARN, tag, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(tag, msg);
        Debug.log(android.util.Log.DEBUG, tag, msg);
    }

    public static void d(String tag, Throwable th) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(tag, "", th);
        Debug.log(android.util.Log.DEBUG, tag,
                th.getMessage() + "\n" +
                        android.util.Log.getStackTraceString(th));
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.e(tag, msg);
        Debug.log(android.util.Log.ERROR, tag, msg);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(tag, msg);
        Debug.log(android.util.Log.INFO, tag, msg);
    }
}

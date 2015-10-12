package com.fieldnation;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    public static void v(String tag, String msg) {
        //android.util.Log.v(tag, msg);
        Debug.log(android.util.Log.VERBOSE, tag, msg);
    }

    public static void w(String tag, String msg) {
        //android.util.Log.w(tag, msg);
        Debug.log(android.util.Log.WARN, tag, msg);
    }

    public static void d(String tag, String msg) {
        //android.util.Log.d(tag, msg);
        Debug.log(android.util.Log.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable th) {
        //android.util.Log.d(tag, msg, th);
        Debug.log(android.util.Log.DEBUG, tag,
                msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(String tag, String msg) {
        //android.util.Log.e(tag, msg);
        Debug.log(android.util.Log.ERROR, tag, msg);
    }

    public static void i(String tag, String msg) {
        //android.util.Log.i(tag, msg);
        Debug.log(android.util.Log.INFO, tag, msg);
    }
}

package com.fieldnation;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    public static void v(String tag, String msg) {
        Debug.log(android.util.Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, Throwable th) {
        Debug.log(android.util.Log.VERBOSE, tag,
                th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void w(String tag, String msg) {
        Debug.log(android.util.Log.WARN, tag, msg);
    }

    public static void d(String tag, String msg) {
        Debug.log(android.util.Log.DEBUG, tag, msg);
    }

    public static void d(String tag, Throwable th) {
        Debug.log(android.util.Log.DEBUG, tag,
                th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(String tag, String msg) {
        Debug.log(android.util.Log.ERROR, tag, msg);
    }

    public static void i(String tag, String msg) {
        Debug.log(android.util.Log.INFO, tag, msg);
    }
}

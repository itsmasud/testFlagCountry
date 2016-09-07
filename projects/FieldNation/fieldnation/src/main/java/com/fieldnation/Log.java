package com.fieldnation;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    public static void v(String tag, String msg) {
        println(android.util.Log.VERBOSE, tag, msg);
    }

    public static void v(String tag, Throwable th) {
        println(android.util.Log.VERBOSE, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void v(String tag, String msg, Throwable tr) {
        println(android.util.Log.VERBOSE, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
    }

    public static void d(String tag, String msg) {
        println(android.util.Log.DEBUG, tag, msg);
    }

    public static void d(String tag, Throwable th) {
        println(android.util.Log.DEBUG, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void d(String tag, String msg, Throwable th) {
        println(android.util.Log.DEBUG, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void w(String tag, String msg) {
        println(android.util.Log.WARN, tag, msg);
    }

    public static void w(String tag, Throwable th) {
        println(android.util.Log.WARN, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void w(String tag, String msg, Throwable th) {
        println(android.util.Log.WARN, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(String tag, String msg) {
        println(android.util.Log.ERROR, tag, msg);
    }

    public static void e(String tag, Throwable th) {
        println(android.util.Log.ERROR, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(String tag, String msg, Throwable th) {
        println(android.util.Log.ERROR, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void i(String tag, String msg) {
        println(android.util.Log.INFO, tag, msg);
    }

    public static void i(String tag, Throwable th) {
        println(android.util.Log.INFO, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void i(String tag, String msg, Throwable th) {
        println(android.util.Log.INFO, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void println(int priority, String tag, String msg) {
        Debug.log(priority, tag, msg);
    }
}

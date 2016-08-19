package com.fieldnation.fnlog;

/**
 * Created by Michael Carver on 2/12/2015.
 */
public class Log {

    private static LogRoller ROLLER = new DefaultRoller();

    public static void setRoller(final LogRoller roller) {
        ROLLER = roller;
    }

    public static void v(final String tag, final String msg) {
        println(android.util.Log.VERBOSE, tag, msg);
    }

    public static void v(final String tag, final Throwable th) {
        println(android.util.Log.VERBOSE, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void v(final String tag, final String msg, final Throwable tr) {
        println(android.util.Log.VERBOSE, tag, msg + '\n' + android.util.Log.getStackTraceString(tr));
    }

    public static void d(final String tag, final String msg) {
        println(android.util.Log.DEBUG, tag, msg);
    }

    public static void d(final String tag, final Throwable th) {
        println(android.util.Log.DEBUG, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void d(final String tag, final String msg, final Throwable th) {
        println(android.util.Log.DEBUG, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void w(final String tag, final String msg) {
        println(android.util.Log.WARN, tag, msg);
    }

    public static void w(final String tag, final Throwable th) {
        println(android.util.Log.WARN, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void w(final String tag, final String msg, final Throwable th) {
        println(android.util.Log.WARN, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(final String tag, final String msg) {
        println(android.util.Log.ERROR, tag, msg);
    }

    public static void e(final String tag, final Throwable th) {
        println(android.util.Log.ERROR, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void e(final String tag, final String msg, final Throwable th) {
        println(android.util.Log.ERROR, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void i(final String tag, final String msg) {
        println(android.util.Log.INFO, tag, msg);
    }

    public static void i(final String tag, final Throwable th) {
        println(android.util.Log.INFO, tag, th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void i(final String tag, final String msg, final Throwable th) {
        println(android.util.Log.INFO, tag, msg + "\n" + android.util.Log.getStackTraceString(th));
    }

    public static void logException(final Throwable throwable) {
        ROLLER.logException(throwable);
    }

    public static void println(final int priority, final String tag, final String msg) {
        ROLLER.println(priority, tag, msg);
    }
}

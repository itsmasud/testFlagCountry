package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.Stopwatch;

/**
 * Created by mc on 11/17/16.
 */

class WebTransactionDispatcher {
    private static final String TAG = "WebTransactionDispatcher";

    public static void queued(Context context, String listenerName, WebTransaction transaction) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.preOnQueued(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "queued time: " + stopwatch.finish());
    }

    public static void start(Context context, String listenerName, WebTransaction transaction) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.preOnStart(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "start time: " + stopwatch.finish());
    }

    public static void paused(Context context, String listenerName, WebTransaction transaction) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.preOnPaused(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "paused time: " + stopwatch.finish());
    }

    public static WebTransactionListener.Result complete(Context context, String listenerName, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        Log.v(TAG, "complete");
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            try {
                Class<?> clazz = context.getClassLoader().loadClass(listenerName);

                WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                        .newInstance((Object[]) null);

                return handler.preComplete(context, transaction, resultData, throwable);

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return WebTransactionListener.Result.DELETE;
        } finally {
            Log.v(TAG, "complete time: " + stopwatch.finish());
        }
    }

    public static void progress(Context context, String listenerName, WebTransaction transaction, long pos, long size, long time) {
        Log.v(TAG, "progress");
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.preOnProgress(context, transaction, pos, size, time);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "progress time: " + stopwatch.finish());
    }
}

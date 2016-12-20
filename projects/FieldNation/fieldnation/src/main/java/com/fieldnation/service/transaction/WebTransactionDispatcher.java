package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnhttpjson.HttpResult;

/**
 * Created by mc on 11/17/16.
 */

public class WebTransactionDispatcher {
    private static final String TAG = "WebTransactionDispatcher";

    public static void start(Context context, String listenerName, WebTransaction transaction) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.onStart(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static WebTransactionListener.Result complete(Context context, String listenerName, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.preComplete(context, transaction, resultData, throwable);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return WebTransactionListener.Result.DELETE;
    }

    public static void progress(Context context, String listenerName, WebTransaction transaction, long pos, long size, long time) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(listenerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.onProgress(context, transaction, pos, size, time);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}

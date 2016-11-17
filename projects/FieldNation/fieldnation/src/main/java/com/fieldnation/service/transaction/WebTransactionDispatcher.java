package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by mc on 11/17/16.
 */

public class WebTransactionDispatcher {
    private static final String TAG = "WebTransactionDispatcher";

    public static WebTransactionListener.Result start(Context context, String handlerName, WebTransaction transaction) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.onStart(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return WebTransactionListener.Result.DELETE;
    }

    public static WebTransactionListener.Result complete(Context context, String handlerName, WebTransaction transaction, HttpResult resultData) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.onComplete(context, transaction, resultData);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return WebTransactionListener.Result.DELETE;
    }

    public static WebTransactionListener.Result fail(Context context, String handlerName, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        //Log.v(TAG, "fail: " + handlerName + "/" + transaction.getRequest().display());

        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.onFail(context, transaction, resultData, throwable);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return WebTransactionListener.Result.DELETE;
    }

    public static void progress(Context context, String handlerName, WebTransaction transaction, long pos, long size, long time) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionListener handler = (WebTransactionListener) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.onProgress(context, transaction, pos, size, time);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}

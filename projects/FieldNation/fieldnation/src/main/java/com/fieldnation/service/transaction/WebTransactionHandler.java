package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class WebTransactionHandler {
    private static final String TAG = "WebTransactionHandler";

    public enum Result {
        REQUEUE, CONTINUE, DELETE
    }

    public static Result startTransaction(Context context, String handlerName, WebTransaction transaction) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.handleStart(context, transaction);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.DELETE;
    }

    public static Result completeTransaction(Context context, String handlerName, WebTransaction transaction, HttpResult resultData) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.handleResult(context, transaction, resultData);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.DELETE;
    }

    public static Result failTransaction(Context context, String handlerName, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        //Log.v(TAG, "failTransaction: " + handlerName + "/" + transaction.getRequest().display());

        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.handleFail(context, transaction, resultData, throwable);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.DELETE;
    }

    public static void transactionProgress(Context context, String handlerName, WebTransaction transaction, long pos, long size, long time) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.handleProgress(context, transaction, pos, size, time);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public Result handleStart(Context context, WebTransaction transaction) {
        return Result.CONTINUE;
    }

    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        return Result.CONTINUE;
    }

    public abstract Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable);

    public void handleProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
    }
}

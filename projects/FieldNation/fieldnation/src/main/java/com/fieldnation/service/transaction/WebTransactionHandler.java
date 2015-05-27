package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class WebTransactionHandler {

    public enum Result {
        REQUEUE, FINISH, ERROR
    }

    public static Result startTransaction(Context context, String handlerName, WebTransaction transaction) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.handleStart(context, transaction);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.ERROR;
    }

    public static Result completeTransaction(Context context, String handlerName, WebTransaction transaction, HttpResult resultData) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            return handler.handleResult(context, transaction, resultData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.ERROR;
    }

    public Result handleStart(Context context, WebTransaction transaction) {
        return Result.FINISH;
    }

    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        return Result.FINISH;
    }
}

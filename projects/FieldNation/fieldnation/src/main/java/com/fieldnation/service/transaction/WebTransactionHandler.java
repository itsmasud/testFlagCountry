package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class WebTransactionHandler {

    public static void completeTransaction(Context context, Listener listener, String handlerName, WebTransaction transaction, HttpResult resultData) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            WebTransactionHandler handler = (WebTransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.handleResult(context, listener, transaction, resultData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData);

    public interface Listener {
        public void onComplete(WebTransaction trans);

        public void requeue(WebTransaction trans);

        public void onError(WebTransaction trans);
    }
}

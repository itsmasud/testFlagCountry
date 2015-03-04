package com.fieldnation.service.transaction;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class TransactionHandler {

    public static void completeTransaction(Context context, Listener listener, String handlerName, Transaction transaction, Bundle resultData) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(handlerName);

            TransactionHandler handler = (TransactionHandler) clazz.getConstructor((Class<?>[]) null)
                    .newInstance((Object[]) null);

            handler.handleResult(context, listener, transaction, resultData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void handleResult(Context context, Listener listener, Transaction transaction, Bundle resultData);

    public interface Listener {
        public void onComplete();
    }
}

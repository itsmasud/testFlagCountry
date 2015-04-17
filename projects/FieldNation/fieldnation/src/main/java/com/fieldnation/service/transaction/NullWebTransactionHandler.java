package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 4/17/2015.
 */
public class NullWebTransactionHandler extends WebTransactionHandler {
    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        listener.onComplete(transaction);
    }
}

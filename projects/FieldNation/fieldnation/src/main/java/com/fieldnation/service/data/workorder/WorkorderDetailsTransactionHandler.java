package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderDetailsTransactionHandler extends WebTransactionHandler {

    public static byte[] generateParams() {
        return null;
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {

    }
}

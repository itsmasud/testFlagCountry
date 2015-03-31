package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael on 3/31/2015.
 */
public class WorkorderActionTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {

    public static byte[] generateParams(String topicId) {
        return topicId.getBytes();
    }

    @Override
    public void handleResult(Context context, WebTransactionHandler.Listener listener, WebTransaction transaction, HttpResult resultData) {

    }
}

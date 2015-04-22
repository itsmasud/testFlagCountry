package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael on 4/9/2015.
 */
public class DeliverableDeleteTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {

    public static byte[] generateParams(long workorderId) {
        return (workorderId + "").getBytes();
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        long workorderId = Long.parseLong(new String(transaction.getHandlerParams()));

        WorkorderTransactionBuilder.getWorkorder(context, workorderId, false);

        return Result.FINISH;
    }
}

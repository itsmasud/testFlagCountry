package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 4/17/2015.
 */
public class NullWebTransactionHandler extends WebTransactionHandler {
    private static final String TAG = "NullWebTransactionHandler";

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        Log.v(TAG, transaction.getKey());
        Log.v(TAG, resultData.getResultsAsString());
        return Result.FINISH;
    }
}

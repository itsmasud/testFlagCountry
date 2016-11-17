package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public abstract class WebTransactionListener {
    private static final String TAG = "WebTransactionListener";

    public enum Result {
        RETRY, CONTINUE, DELETE
    }

    public Result onStart(Context context, WebTransaction transaction) {
        return Result.CONTINUE;
    }

    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        return Result.CONTINUE;
    }

    public abstract Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable);

    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
    }
}

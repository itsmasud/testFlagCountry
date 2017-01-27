package com.fieldnation.data.bv2.templates;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

/**
 * Created by mc on 1/27/17.
 */

public class TransactionListener extends WebTransactionListener {
    private static final String TAG = "TransactionListener";

    /**
     * @param apiClass     (Required) the api function's class that spawned the call
     * @param apiFunction  (Required) The api function's name that spawned the call
     * @param successClass (Optional) the data model that a successful call returns
     * @param errorClass   (Optional) the data model that an error response returns
     * @return
     */
    public static byte[] params(Class<?> apiClass, String apiFunction, Class<?> successClass, Class<?> errorClass) {
        try {
            TransactionParams params = new TransactionParams();

            params.apiClassName = apiClass.getName();
            params.apiFunction = apiFunction;

            if (successClass != null)
                params.successClassName = successClass.getName();

            if (errorClass != null)
                params.errorClassName = errorClass.getName();

            return params.toJson().toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public void onStart(Context context, WebTransaction transaction) {
        super.onStart(context, transaction);
    }

    @Override
    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
        super.onProgress(context, transaction, pos, size, time);
    }

    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        if (result == Result.CONTINUE) {
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));

                Bundle bundle = new Bundle();

                bundle.putString("key", transaction.getKey());

                if (httpResult.isFile()) {
                } else {
                    bundle.putByteArray("successByteArray", httpResult.getByteArray());
                }

                TopicService.dispatchEvent(context, "TOPIC_ID_API_V2/" + transaction.getKey(), bundle, Sticky.TEMP);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            return Result.CONTINUE;
        } else if (result == Result.DELETE) {
            Log.v(TAG, "break!");
        }
        return super.onComplete(context, result, transaction, httpResult, throwable);
    }
}

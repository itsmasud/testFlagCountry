package com.fieldnation.v2.data.listener;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.StreamUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by mc on 1/27/17.
 */

public class TransactionListener extends WebTransactionListener {
    private static final String TAG = "TransactionListener";

    /**
     * @param apiClass    (Required) the api function's class that spawned the call
     * @param apiFunction (Required) The api function's name that spawned the call
     * @return
     */
    public static byte[] params(String topicId, Class<?> apiClass, String apiFunction) {
        try {
            TransactionParams params = new TransactionParams();

            params.topicId = topicId;
            params.apiClassName = apiClass.getName();
            params.apiFunction = apiFunction;

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
                bundle.putParcelable("params", params);

                if (httpResult.isFile()) {
                    File file = httpResult.getFile();
                    bundle.putByteArray("data", StreamUtils.readAllFromStream(new FileInputStream(file), (int) file.length(), 1000));
                } else {
                    bundle.putByteArray("data", httpResult.getByteArray());
                }
                bundle.putBoolean("success", true);

                TopicService.dispatchEvent(context, params.topicId, bundle, Sticky.TEMP);

                String method = new JsonObject(transaction.getRequestString()).getString("method");
                if (method.equals("GET")) {
                    StoredObject.put(context, App.getProfileId(), "V2_PARAMS", transaction.getKey(), params.toJson().toByteArray(), true);
                    if (httpResult.isFile()) {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), httpResult.getFile(), transaction.getKey(), true);
                    } else {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), httpResult.getByteArray(), true);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                // TODO error!
            }

            return Result.CONTINUE;
        } else if (result == Result.DELETE) {
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(transaction.getListenerParams()));

                Bundle bundle = new Bundle();
                bundle.putParcelable("params", params);

                if (httpResult.isFile()) {
                    File file = httpResult.getFile();
                    bundle.putByteArray("data", StreamUtils.readAllFromStream(new FileInputStream(file), (int) file.length(), 1000));
                } else {
                    bundle.putByteArray("data", httpResult.getByteArray());
                }
                bundle.putBoolean("success", false);

                TopicService.dispatchEvent(context, params.topicId, bundle, Sticky.TEMP);

                String method = new JsonObject(transaction.getRequestString()).getString("method");
                if (method.equals("GET")) {
                    StoredObject.put(context, App.getProfileId(), "V2_PARAMS", transaction.getKey(), params.toJson().toByteArray(), true);
                    if (httpResult.isFile()) {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), httpResult.getFile(), transaction.getKey(), true);
                    } else {
                        StoredObject.put(context, App.getProfileId(), "V2_DATA", transaction.getKey(), httpResult.getByteArray(), true);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                // TODO error!
            }

            return Result.DELETE;
        } else if (result == Result.RETRY) {
            Log.v(TAG, "break!");
            return Result.RETRY;
        }
        return super.onComplete(context, result, transaction, httpResult, throwable);
    }
}

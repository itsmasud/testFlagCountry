package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Michael on 4/9/2015.
 */
public class DeliverableTransactionListener extends WebTransactionListener implements WorkorderConstants {
    private static final String TAG = "DeliverableTransactionListener";

    public static byte[] pChange(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pChange");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pGet(long workorderId, long deliverableId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("workorderId", workorderId);
            obj.put("deliverableId", deliverableId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    @Override
    public Result onSuccess(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        result = super.onSuccess(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pChange":
                    return onSuccessChange(context, result, transaction, params, httpResult, throwable);
                case "pGet":
                    return onSuccessGet(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return result;
    }

    public Result onSuccessChange(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");

        WorkorderTransactionBuilder.get(context, workorderId, false);

        WorkorderClient.get(context, workorderId, false, false);

        return Result.CONTINUE;
    }

    public Result onSuccessGet(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long deliverableId = params.getLong("deliverableId");
        byte[] data = httpResult.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_DELIVERABLE, deliverableId, data);

        WorkorderDispatch.getDeliverable(context, new JsonObject(data), workorderId, deliverableId, false, transaction.isSync());

        return Result.CONTINUE;
    }
}
package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
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
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        if (result != Result.CONTINUE)
            return result;

        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pChange":
                    return onChange(context, result, transaction, params, httpResult, throwable);
                case "pGet":
                    return onGet(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    public Result onChange(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");

        WorkorderTransactionBuilder.get(context, workorderId, false);

        WorkorderClient.get(context, workorderId, false, false);

        return Result.CONTINUE;
    }

    public Result onGet(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long deliverableId = params.getLong("deliverableId");
        byte[] data = httpResult.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_DELIVERABLE, deliverableId, data);

        WorkorderDispatch.getDeliverable(context, new JsonObject(data), workorderId, deliverableId, false, transaction.getType() == WebTransaction.Type.SYNC);

        return Result.CONTINUE;
    }
}
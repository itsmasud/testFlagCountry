package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {
    private static final String TAG = "WorkorderTransactionHandler";

    // parameter generators
    public static byte[] pDetails(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pDetails");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pGetSignature(long workorderId, long signatureId) {
        try {
            JsonObject obj = new JsonObject("action", "pGetSignature");
            obj.put("workorderId", workorderId);
            obj.put("signatureId", signatureId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pCheckIn(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pCheckIn");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // plumbing
    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            if (action.equals("pDetails")) {
                handleDetails(context, listener, transaction, params, resultData);
            } else if (action.equals("pGetSignature")) {
                handleGetSignature(context, listener, transaction, params, resultData);
            } else if (action.equals("pCheckIn")) {
                handleCheckIn(context, listener, transaction, params, resultData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        listener.requeue(transaction);
    }

    private void handleCheckIn(Context context, Listener listener, WebTransaction transaction,
                               JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCheckIn");
        long workorderId = params.getLong("workorderId");

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_CHECKIN);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putByteArray(PARAM_DATA_BYTE_ARRAY, resultData.getResultsAsByteArray());
        TopicService.dispatchEvent(context, PARAM_ACTION_CHECKIN, bundle, true);
        listener.onComplete(transaction);
    }

    // individual commands
    private void handleDetails(Context context, Listener listener, WebTransaction transaction,
                               JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getResultsAsByteArray();

        Log.v(TAG, "handleDetails workorderId:" + workorderId);

        // store it in the store
        StoredObject.put(context, PSO_WORKORDER, workorderId, workorderData);

        JsonObject workorder = new JsonObject(workorderData);

        Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

        // dispatch the event
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorder);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS + "/" + workorderId, bundle, true);
        listener.onComplete(transaction);
    }

    private void handleGetSignature(Context context, Listener listener, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = resultData.getResultsAsByteArray();

        //store the signature data
        StoredObject.put(context, PSO_SIGNATURE, signatureId + "", data);

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonObject(data));
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, bundle, true);
        listener.onComplete(transaction);
    }
}

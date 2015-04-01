package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {

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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        listener.requeue(transaction);
    }


    // individual commands
    private void handleDetails(Context context, Listener listener, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getResultsAsByteArray();

        // store it in the store
        StoredObject.put(context, PSO_WORKORDER, workorderId + "", workorderData);

        JsonObject workorder = new JsonObject(workorderData);

        List<Transform> transList = Transform.getObjectTransforms(context, "Workorder", workorderId);
        for (int i = 0; i < transList.size(); i++) {
            Transform t = transList.get(i);

            JsonObject tObj = new JsonObject(t.getData());
            workorder.deepmerge(tObj);
        }

        // dispatch the event
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
        bundle.putByteArray(PARAM_DATA, workorder.toByteArray());
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
        bundle.putByteArray(PARAM_DATA, data);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, bundle, true);
        listener.onComplete(transaction);
    }
}

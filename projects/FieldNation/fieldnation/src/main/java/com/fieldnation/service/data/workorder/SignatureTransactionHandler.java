package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/30/2015.
 */
public class SignatureTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {

    public static byte[] generateParams(long workorderId, long signatureId) {
        try {
            JsonObject obj = new JsonObject();
            obj.put("workorderId", workorderId);
            obj.put("signatureId", signatureId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
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
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        listener.requeue(transaction);
    }
}

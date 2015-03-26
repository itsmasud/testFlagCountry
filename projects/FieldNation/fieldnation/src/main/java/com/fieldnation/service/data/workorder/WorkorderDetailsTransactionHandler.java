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
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderDetailsTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {

    public static byte[] generateParams(long id) {
        try {
            JsonObject obj = new JsonObject("workorderId", id);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            long workorderId = params.getLong("workorderId");
            byte[] workorderData = resultData.getResultsAsByteArray();

            // store it in the store
            StoredObject.put(context, PSO_WORKORDER, workorderId + "", workorderData);

            // dispatch the event
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
            bundle.putByteArray(PARAM_DATA, workorderData);
            bundle.putLong(PARAM_ID, workorderId);
            TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS + "/" + workorderId, bundle, true);
            listener.onComplete(transaction);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        listener.requeue(transaction);
    }
}

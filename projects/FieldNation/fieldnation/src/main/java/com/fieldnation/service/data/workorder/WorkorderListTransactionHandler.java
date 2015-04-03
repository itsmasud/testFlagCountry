package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.utils.Stopwatch;

import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class WorkorderListTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {
    private static final String TAG = "WorkorderListTransactionHandler";

    public static byte[] generateParams(int page, String selector) {
        JsonObject obj = new JsonObject();
        try {
            obj.put("page", page);
            obj.put("selector", selector);
        } catch (Exception ex) {
        }

        return obj.toByteArray();
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "handleResult");
        // get the basics, send out the event
        int page = 0;
        String selector = "";

        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            page = obj.getInt("page");
            selector = obj.getString("selector");

            byte[] bdata = resultData.getResultsAsByteArray();

            StoredObject.put(context, PSO_WORKORDER_LIST + selector, page, bdata);

            JsonArray ja = new JsonArray(bdata);
            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                List<Transform> transList = Transform.getObjectTransforms(context, PSO_WORKORDER, json.getLong("workorderId"));
                for (int j = 0; j < transList.size(); j++) {
                    Transform t = transList.get(j);
                    Log.v(TAG, "handleResult, trans: " + new String(t.getData()));
                    JsonObject to = new JsonObject(t.getData());
                    json.deepmerge(to);
                }
            }

            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_DATA, ja.toByteArray());
            bundle.putInt(PARAM_PAGE, page);
            bundle.putString(PARAM_LIST_SELECTOR, selector);
            bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
            TopicService.dispatchEvent(context, PARAM_ACTION_LIST + "/" + selector, bundle, true);
            listener.onComplete(transaction);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.v(TAG, "handleResult " + watch.finish());
    }

}

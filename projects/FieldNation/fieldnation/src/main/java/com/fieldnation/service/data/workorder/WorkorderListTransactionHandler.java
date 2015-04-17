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
            // <free>
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            page = obj.getInt("page");
            selector = obj.getString("selector");
            byte[] bdata = resultData.getResultsAsByteArray();
            Log.v(TAG, "page: " + page + " selector:" + selector);
            // </free>

            // <350ms>
            StoredObject.put(context, PSO_WORKORDER_LIST + selector, page, bdata);
            // </350ms>

            // 3.3 seconds
            Stopwatch processTime = new Stopwatch(true);
            // <588ms>
            JsonArray ja = new JsonArray(bdata);
            // </588ms>

            Stopwatch transformQuery = new Stopwatch(false);
            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                // <3s>
                transformQuery.start();
                Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
                transformQuery.pause();
                // </3s>
            }
            Log.v(TAG, "transformQuery time: " + transformQuery.finish());
            Log.v(TAG, "process time: " + processTime.finish());
            // /3.3 seconds

            // <10s>
            Stopwatch createBundleTime = new Stopwatch(true);
            Bundle bundle = new Bundle();

            Stopwatch toByteArray = new Stopwatch(true);
            bundle.putParcelable(PARAM_DATA_PARCELABLE, ja);
//            bundle.putByteArray(PARAM_DATA, bdata);
            Log.v(TAG, "toByteArray time: " + toByteArray.finish());

            bundle.putInt(PARAM_PAGE, page);
            bundle.putString(PARAM_LIST_SELECTOR, selector);
            bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
            Log.v(TAG, "createBundle time: " + createBundleTime.finish());

            Stopwatch dispatchTime = new Stopwatch(true);
            TopicService.dispatchEvent(context, PARAM_ACTION_LIST + "/" + selector, bundle, true);
            listener.onComplete(transaction);
            Log.v(TAG, "dispatch time: " + dispatchTime.finish());
            // </10s>
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.requeue(transaction);
        }
        Log.v(TAG, "handleResult time: " + watch.finish());

    }

}

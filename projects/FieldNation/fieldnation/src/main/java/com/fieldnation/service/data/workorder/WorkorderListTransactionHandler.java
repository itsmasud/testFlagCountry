package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
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
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "handleResult");
        // get the basics, send out the event
        int page = 0;
        String selector = "";

        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            page = obj.getInt("page");
            selector = obj.getString("selector");
            byte[] bdata = resultData.getByteArray();
            Log.v(TAG, "page: " + page + " selector:" + selector);

            StoredObject.put(context, PSO_WORKORDER_LIST + selector, page, bdata);

            JsonArray ja = new JsonArray(bdata);

            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
            }

            WorkorderDispatch.workorderList(context, ja, page, selector, transaction.isSync());

            return Result.FINISH;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.v(TAG, "handleResult time: " + watch.finish());
        return Result.REQUEUE;
    }

}

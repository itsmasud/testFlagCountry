package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

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
        Log.v(TAG, "handleResult");
        // get the basics, send out the event
        int page = 0;
        String selector = "";

        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            page = obj.getInt("page");
            selector = obj.getString("selector");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        byte[] bdata = resultData.getResultsAsByteArray();
        StoredObject obj = StoredObject.put(context, PSO_WORKORDER_LIST + selector, page + "", bdata);
        Bundle bundle = new Bundle();
        bundle.putByteArray(PARAM_DATA, obj.getData());
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_LIST_SELECTOR, selector);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST + "/" + selector, bundle, true);
        listener.onComplete(transaction);
        // now parse all the workorders?
//
//        String data = new String(resultData.getResultsAsByteArray());
//        // TODO need to figure out how to detect end of list, and delete extra pages
//
//        JsonArray objects = null;
//        try {
//            objects = new JsonArray(data);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            // TODO do something about this. Set up a transaction to re-request this page?
//            listener.onError(transaction);
//        }
//
//        List<Workorder> workorders = new LinkedList<>();
//        for (int i = 0; i < objects.size(); i++) {
//            try {
//                workorders.add(Workorder.fromJson(objects.getJsonObject(i)));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // if response list is empty, then delete this page, and all the others after it
//        if (workorders.size() == 0) {
//            int i = page;
//            while (StoredObject.delete(context, "Workorder" + listName + "Page", i + "")) {
//                i++;
//            }
//        }
//
//        // TODO query the next page
//
//        // update the list
//        JsonArray ja = new JsonArray();
//        for (int i = 0; i < workorders.size(); i++) {
//            ja.add(workorders.get(i).getWorkorderId());
//        }
//        StoredObject.put(
//                context,
//                "Workorder" + listName + "Page",
//                page + "", null,
//                ja.toByteArray());
//
//        // update all the work orders
//        for (int i = 0; i < workorders.size(); i++) {
//            Workorder wo = workorders.get(i);
//            StoredObject so = StoredObject.get(context, "Workorder", wo.getWorkorderId() + "");
//            if (so == null) {
//                StoredObject.put(context, "Workorder", wo.getWorkorderId() + "", null, wo.toJson().toByteArray());
//            } else {
//                try {
//                    JsonObject json = new JsonObject(so.getData());
//                    json.merge(wo.toJson(), true, true);
//                    so.setData(json.toByteArray());
//                    so.save(context);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    StoredObject.put(context, "Workorder", wo.getWorkorderId() + "", null, wo.toJson().toByteArray());
//                }
//            }
//        }

    }

}

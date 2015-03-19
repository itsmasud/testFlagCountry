package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionConstants;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class WorkorderListTransactionHandler extends WebTransactionHandler implements WebServiceConstants, WebTransactionConstants {

    public static byte[] generateParams(int page, String listName) {
        JsonObject obj = new JsonObject();
        try {
            obj.put("page", page);
            obj.put("listName", listName);
        } catch (Exception ex) {
        }

        return obj.toByteArray();
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        int page = 0;

        String listName = "";
        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            page = obj.getInt("page");
            listName = obj.getString("listName");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
//
//        JsonArray objects = null;
//        try {
//            objects = new JsonArray(data);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            // TODO do something about this. Set up a transaction to re-request this page?
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
//        listener.onComplete();
    }

}

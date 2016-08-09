package com.fieldnation.service.data.restv2;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestTransactionHandler extends WebTransactionHandler {
    private static final String TAG = "RestTransactionHandler";

    public static byte[] pList(String resultTag, String objectType, Sticky sticky) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("resultTag", resultTag);
            obj.put("objectType", objectType);
            obj.put("sticky", sticky.ordinal());
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pObject(String resultTag, Sticky sticky) {
        try {
            JsonObject obj = new JsonObject("action", "pObject");
            obj.put("resultTag", resultTag);
            obj.put("sticky", sticky.ordinal());
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pList":
                    return list(context, transaction, resultData, params);
                case "pObject":
                    return object(context, transaction, resultData, params);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.REQUEUE;
        }
        return Result.CONTINUE;
    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        // TODO, need to handle these
        return null;
    }

    private static Result object(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        String resultTag = params.getString("resultTag");
        Sticky sticky = Sticky.values()[params.getInt("sticky")];

        JsonObject json = resultData.getJsonObject();
        String id = json.getString("id");
        String objectType = json.getString("object");

        // fast
        RestDispatch.object(context, resultTag, objectType, id, json, sticky, transaction.isSync());

        // slow
        StoredObject.put(App.getProfileId(), objectType, id, resultData.getByteArray());

        return Result.CONTINUE;

    }

    private static Result list(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        String resultTag = params.getString("resultTag");
        String objectType = params.getString("objectType");
        Sticky sticky = Sticky.values()[params.getInt("sticky")];

        JsonObject envelope = resultData.getJsonObject();

        // TODO not sure I wanna do this
/*
        JsonArray list = envelope.getJsonArray("list");
        for (int i = 0; i < list.size(); i++) {
            JsonObject obj = list.getJsonObject(i);
            String id = obj.getString("id");

            // do this first, it's fast
            RestDispatch.object(context, resultTag, objectType, id, obj, transaction.isSync());
        }

        for (int i = 0; i < list.size(); i++) {
            JsonObject obj = list.getJsonObject(i);
            String id = obj.getString("id");

            // do this second, it's slow
            StoredObject.put(context, objectType, id, obj.toByteArray());
        }
*/

        RestDispatch.list(context, resultTag, objectType, envelope, sticky, transaction.isSync());

        StoredObject.put(App.getProfileId(), objectType + "List", envelope.getLong("page"), envelope.toByteArray());

        return Result.CONTINUE;
    }
}

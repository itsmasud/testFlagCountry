package com.fieldnation.service.data.restv2;

import android.content.Context;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestTransactionHandler extends WebTransactionHandler {

    public static byte[] pList(String resultTag) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("resultTag", resultTag);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.REQUEUE;
        }
        return Result.FINISH;
    }

    private static Result list(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) throws ParseException {
        String resultTag = params.getString("resultTag");

        JsonArray ja = new JsonArray(resultData.getByteArray());

        for (int i = 0; i < ja.size(); i++) {
            JsonObject obj = ja.getJsonObject(i);
            String type = obj.getString("object");
            String id = obj.getString("id");

            // todo create object from json

            RestDispatch.object(context, resultTag, type, id, null, transaction.isSync());
        }

        return Result.FINISH;
    }
}

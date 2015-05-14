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

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionHandler extends WebTransactionHandler implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionHandler";

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

    public static byte[] pAction(long workorderId, String action) {
        try {
            JsonObject obj = new JsonObject("action", "pAction");
            obj.put("workorderId", workorderId);
            obj.put("param", action);
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

    public static byte[] pMessageList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pMessageList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pAlertList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pAlertList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pTaskList(long workorderId){
        try {
            JsonObject obj = new JsonObject("action", "pTaskList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // plumbing
    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDetails":
                    return handleDetails(context, transaction, params, resultData);
                case "pGetSignature":
                    return handleGetSignature(context, transaction, params, resultData);
                case "pAction":
                    return handleAction(context, transaction, params, resultData);
                case "pMessageList":
                    return handleMessageList(context, transaction, params, resultData);
                case "pAlertList":
                    return handleAlertList(context, transaction, params, resultData);
                case "pTaskList":
                    return handleTaskList(context, transaction, params, resultData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        WorkorderDispatch.action(context, workorderId, action);

        return Result.FINISH;
    }

    private Result handleMessageList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleMessageList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listMessages(context, workorderId, new JsonArray(data), transaction.isSync());

        StoredObject.put(context, PSO_MESSAGE_LIST, workorderId, data);

        return Result.FINISH;
    }

    private Result handleAlertList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAlertList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(data), transaction.isSync());

        StoredObject.put(context, PSO_ALERT_LIST, workorderId, data);

        return Result.FINISH;
    }

    private Result handleTaskList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleTaskList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listTasks(context, workorderId, new JsonArray(data), transaction.isSync());

        StoredObject.put(context, PSO_TASK_LIST, workorderId, data);

        return Result.FINISH;
    }

    // individual commands
    private Result handleDetails(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getByteArray();

        Log.v(TAG, "handleDetails workorderId:" + workorderId);


        JsonObject workorder = new JsonObject(workorderData);

        Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

        // dispatch the event
        WorkorderDispatch.get(context, workorder, workorderId, transaction.isSync());

        // store it in the store
        StoredObject.put(context, PSO_WORKORDER, workorderId, workorderData);

        return Result.FINISH;
    }

    private Result handleGetSignature(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = resultData.getByteArray();


        WorkorderDispatch.signature(context, new JsonObject(data), workorderId, signatureId, transaction.isSync());

        //store the signature data
        StoredObject.put(context, PSO_SIGNATURE, signatureId, data);

        return Result.FINISH;
    }
}

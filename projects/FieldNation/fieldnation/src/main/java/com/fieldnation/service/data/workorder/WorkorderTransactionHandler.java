package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.Log;
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
public class WorkorderTransactionHandler extends WebTransactionHandler implements WorkorderDataConstants {
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

    public static byte[] pCheckIn(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pCheckIn");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pCheckOut(long workorderId){
        try {
            JsonObject obj = new JsonObject("action", "pCheckOut");
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
            if (action.equals("pDetails")) {
                return handleDetails(context, transaction, params, resultData);
            } else if (action.equals("pGetSignature")) {
                return handleGetSignature(context, transaction, params, resultData);
            } else if (action.equals("pCheckIn")) {
                return handleCheckIn(context, transaction, params, resultData);
            } else if(action.equals("pCheckOut")){
                return handleCheckOut(context, transaction, params, resultData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleCheckIn(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCheckIn");
        long workorderId = params.getLong("workorderId");

        WorkorderDataDispatch.checkIn(context, workorderId, resultData.getResultsAsByteArray());

        return Result.FINISH;
    }

    private Result handleCheckOut(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCheckOut");
        long workorderId = params.getLong("workorderId");

        WorkorderDataDispatch.checkOut(context, workorderId, resultData.getResultsAsByteArray());

        return Result.FINISH;
    }

    // individual commands
    private Result handleDetails(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getResultsAsByteArray();

        Log.v(TAG, "handleDetails workorderId:" + workorderId);

        // store it in the store
        StoredObject.put(context, PSO_WORKORDER, workorderId, workorderData);

        JsonObject workorder = new JsonObject(workorderData);

        Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

        // dispatch the event
        WorkorderDataDispatch.workorder(context, workorder, workorderId);
        return Result.FINISH;
    }

    private Result handleGetSignature(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = resultData.getResultsAsByteArray();

        //store the signature data
        StoredObject.put(context, PSO_SIGNATURE, signatureId + "", data);

        WorkorderDataDispatch.signature(context, new JsonObject(data), workorderId, signatureId);

        return Result.FINISH;
    }
}

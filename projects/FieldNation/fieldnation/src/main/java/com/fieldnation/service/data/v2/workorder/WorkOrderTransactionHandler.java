package com.fieldnation.service.data.v2.workorder;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderTransactionHandler extends WebTransactionHandler implements WorkOrderConstants {
    private static final String TAG = "WorkOrderTransactionHandler";

    /*-************************************************-*/
    /*-             Parameter Generators               -*/
    /*-************************************************-*/
    public static byte[] pSearch(SearchParams searchParams) {
        try {
            JsonObject obj = new JsonObject("action", "pSearch");
            obj.put("SearchParams", searchParams.toJson());
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-************************************-*/
    /*-             Complete               -*/
    /*-************************************-*/

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return resultAction(context, transaction, params, resultData);
                case "pSearch":
                    return resultSearch(context, transaction, params, resultData);
                default:
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result resultAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "resultAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");
        WorkOrderDispatch.action(context, workorderId, action, false);
//        WorkOrderClient.listTasks(context, workorderId, false);

//        if (action.equals("acknowledge-hold")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("closing-notes")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("complete")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("decline")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("delete_request")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("DELETE_LOG")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("incomplete")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("messages/new")) {
//            WorkorderClient.listMessages(context, workorderId, false, false);
//        } else if (action.equals("pay-change")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else if (action.equals("ready")) {
//            return handleDetails(context, transaction, params, resultData);
//        } else {
//            WorkorderClient.get(context, workorderId, false);
//        }

        return Result.CONTINUE;
    }

    private Result resultSearch(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "resultSearch");

        WorkOrderDispatch.search(context,
                SearchParams.fromJson(params.getJsonObject("SearchParams")),
                resultData.getByteArray(), false);

        return Result.CONTINUE;
    }

    /*-**********************************-*/
    /*-             Failed               -*/
    /*-**********************************-*/
    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return failAction(context, transaction, params, resultData);
                case "pSearch":
                    return failSearch(context, transaction, params, resultData);
                default:
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result failAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "failAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        WorkOrderDispatch.action(context, workorderId, action, true);

        return Result.CONTINUE;
    }

    private Result failSearch(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "failSearch");

        WorkOrderDispatch.search(context,
                SearchParams.fromJson(params.getJsonObject("SearchParams")), null, true);

        return Result.CONTINUE;
    }
}

package com.fieldnation.service.data.v2.workorder;

import android.content.Context;

import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderTransactionListener extends WebTransactionListener implements WorkOrderConstants {
    private static final String TAG = "WorkOrderTransactionListener";

    /*-************************************************-*/
    /*-             Parameter Generators               -*/
    /*-************************************************-*/
    public static byte[] pSearch(SavedSearchParams searchParams) {
        try {
            JsonObject obj = new JsonObject("action", "pSearch");
            obj.put("SavedSearchParams", searchParams.toJson());
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
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return onAction(context, result, transaction, params, httpResult, throwable);
                case "pSearch":
                    return onSearch(context, result, transaction, params, httpResult, throwable);
                default:
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result onAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        if (result != Result.CONTINUE) {
            WorkOrderDispatch.action(context, workorderId, action, true);
            return result;
        } else {
            WorkOrderDispatch.action(context, workorderId, action, false);
            return Result.CONTINUE;
        }
    }

    private Result onSearch(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSearch");

        if (result != Result.CONTINUE) {
            WorkOrderDispatch.search(context, SavedSearchParams.fromJson(params.getJsonObject("SavedSearchParams")), null, true);
            return result;
        } else {
            WorkOrderDispatch.search(context,
                    SavedSearchParams.fromJson(params.getJsonObject("SavedSearchParams")),
                    httpResult.getByteArray(), false);
            return Result.CONTINUE;
        }
    }
}
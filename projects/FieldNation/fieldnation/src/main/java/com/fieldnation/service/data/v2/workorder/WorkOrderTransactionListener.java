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
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return onCompleteAction(context, transaction, params, resultData);
                case "pSearch":
                    return onCompleteSearch(context, transaction, params, resultData);
                default:
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result onCompleteAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "onCompleteAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");
        WorkOrderDispatch.action(context, workorderId, action, false);
        return Result.CONTINUE;
    }

    private Result onCompleteSearch(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "onCompleteSearch");
        WorkOrderDispatch.search(context,
                SavedSearchParams.fromJson(params.getJsonObject("SavedSearchParams")),
                resultData.getByteArray(), false);
        return Result.CONTINUE;
    }

    /*-**********************************-*/
    /*-             Failed               -*/
    /*-**********************************-*/
    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pAction":
                    return onFailAction(context, transaction, params, resultData);
                case "pSearch":
                    return onFailSearch(context, transaction, params, resultData);
                default:
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result onFailAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "onFailAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        WorkOrderDispatch.action(context, workorderId, action, true);

        return Result.CONTINUE;
    }

    private Result onFailSearch(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "onFailSearch");

        WorkOrderDispatch.search(context,
                SavedSearchParams.fromJson(params.getJsonObject("SavedSearchParams")), null, true);

        return Result.CONTINUE;
    }
}

package com.fieldnation.service.data.v2.workorder;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
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

    /*-************************************-*/
    /*-             Complete               -*/
    /*-************************************-*/

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
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

    private Result failSearch(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "resultSearch");

        WorkOrderDispatch.search(context,
                SearchParams.fromJson(params.getJsonObject("SearchParams")), null, true);

        return Result.CONTINUE;
    }
}

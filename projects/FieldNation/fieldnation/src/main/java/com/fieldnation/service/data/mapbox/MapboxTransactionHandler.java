package com.fieldnation.service.data.mapbox;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxTransactionHandler extends WebTransactionHandler implements MapboxConstants {
    private static final String TAG = "MapboxTransactionHandler";

    public static byte[] pDirections() {
        try {
            JsonObject obj = new JsonObject("action", "pDirections");
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    @Override
    public Result handleStart(Context context, WebTransaction transaction) {
        return super.handleStart(context, transaction);
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDirections":
                    return handleDirections(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleDirections(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) {

        MapboxDispatch.directions(context, resultData.getByteArray());

        return Result.CONTINUE;
    }

    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        return null;
    }
}

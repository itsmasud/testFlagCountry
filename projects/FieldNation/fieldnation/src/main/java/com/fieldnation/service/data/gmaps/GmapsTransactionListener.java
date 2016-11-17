package com.fieldnation.service.data.gmaps;

import android.content.Context;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class GmapsTransactionListener extends WebTransactionListener implements GmapsConstants {
    private static final String TAG = "GmapsTransactionListener";

    public static byte[] pDirections(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pDirections");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pStaticMapClassic(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pStaticMapClassic");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    @Override
    public Result onStart(Context context, WebTransaction transaction) {
        return super.onStart(context, transaction);
    }

    @Override
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDirections":
                    return handleDirections(context, transaction, params, resultData);
                case "pStaticMapClassic":
                    return handleStaticMapClassic(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleDirections(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDirections");
        GmapsDispatch.directions(context, params.getLong("workorderId"), resultData.getByteArray());
        return Result.CONTINUE;
    }

    private Result handleStaticMapClassic(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        GmapsDispatch.staticMapClassic(context, params.getLong("workorderId"), resultData.getByteArray(), false);
        return Result.CONTINUE;
    }

    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pStaticMapClassic":
                    return handleStaticMapClassicFail(context, transaction, params, resultData);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleStaticMapClassicFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        GmapsDispatch.staticMapClassic(context, params.getLong("workorderId"), null, true);
        return Result.CONTINUE;
    }

}

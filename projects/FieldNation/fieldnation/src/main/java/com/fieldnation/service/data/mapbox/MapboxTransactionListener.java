package com.fieldnation.service.data.mapbox;

import android.content.Context;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxTransactionListener extends WebTransactionListener implements MapboxConstants {
    private static final String TAG = "MapboxTransactionListener";

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
    public Result onSuccess(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        result = super.onSuccess(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDirections":
                    return onSuccessDirections(context, result, transaction, params, httpResult, throwable);
                case "pStaticMapClassic":
                    return onSuccessStaticMapClassic(context, result, transaction, params, httpResult, throwable);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.RETRY;
        }
        return result;
    }

    private Result onSuccessDirections(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessDirections");
        MapboxDispatch.directions(context, params.getLong("workorderId"), httpResult.getByteArray());
        return Result.CONTINUE;
    }

    private Result onSuccessStaticMapClassic(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        MapboxDispatch.staticMapClassic(context, params.getLong("workorderId"), httpResult.getByteArray(), false);
        return Result.CONTINUE;
    }

    @Override
    public Result onFail(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        // we don't want to call super because we don't trust the error messages from mapbox
        // result = super.onFail(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pStaticMapClassic":
                    return onFailStaticMapClassic(context, result, transaction, params, httpResult, throwable);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    private Result onFailStaticMapClassic(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        MapboxDispatch.staticMapClassic(context, params.getLong("workorderId"), null, true);
        return result;
    }

}

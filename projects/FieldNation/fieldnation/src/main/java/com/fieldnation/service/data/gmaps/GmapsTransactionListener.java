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
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDirections":
                    return onDirections(context, result, transaction, params, httpResult, throwable);
                case "pStaticMapClassic":
                    return onStaticMapClassic(context, result, transaction, params, httpResult, throwable);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    private Result onDirections(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onDirections");
        if (result == Result.CONTINUE) {
            GmapsDispatch.directions(context, params.getLong("workorderId"), httpResult.getByteArray());
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onStaticMapClassic(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        if (result == Result.CONTINUE) {
            GmapsDispatch.staticMapClassic(context, params.getLong("workorderId"), httpResult.getByteArray(), false);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            GmapsDispatch.staticMapClassic(context, params.getLong("workorderId"), null, true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }
}

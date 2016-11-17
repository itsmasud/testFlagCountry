package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentTransactionListener extends WebTransactionListener implements PaymentConstants {
    private static final String TAG = "PaymentTransactionListener";

    public static byte[] pList(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pGet(long paymentId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("paymentId", paymentId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public Result onComplete(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject obj = new JsonObject(transaction.getListenerParams());
            String action = obj.getString("action");

            switch (action) {
                case "pList":
                    return handleList(context, transaction, resultData, obj);
                case "pGet":
                    return handleGet(context, transaction, resultData, obj);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return Result.RETRY;
    }

    @Override
    public Result onFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject obj = new JsonObject(transaction.getListenerParams());
            String action = obj.getString("action");

            switch (action) {
                case "pList":
                    PaymentDispatch.list(context, obj.getInt("page"), null, true, transaction.isSync(), true);
                    break;
                case "pGet":
                    PaymentDispatch.get(context, obj.getLong("paymentId"), null, true, transaction.isSync());
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }


    private Result handleList(Context context, WebTransaction transaction, HttpResult resultData,
                              JsonObject params) throws ParseException {
        int page = params.getInt("page");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_PAYMENT_LIST, page, data);

        PaymentDispatch.list(context, page, new JsonArray(data), false, transaction.isSync(), false);
        return Result.CONTINUE;
    }

    private Result handleGet(Context context, WebTransaction transaction, HttpResult resultData,
                             JsonObject params) throws ParseException {
        long paymentId = params.getLong("paymentId");

        byte[] data = resultData.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_PAYMENT, paymentId, data);

        PaymentDispatch.get(context, paymentId, new JsonObject(data), false, transaction.isSync());
        return Result.CONTINUE;
    }
}

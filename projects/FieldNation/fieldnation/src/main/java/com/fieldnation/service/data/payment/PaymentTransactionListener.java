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
    public Result onSuccess(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        result = super.onSuccess(context, result, transaction, httpResult, throwable);
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");

            switch (action) {
                case "pList":
                    return onSuccessList(context, result, transaction, params, httpResult, throwable);
                case "pGet":
                    return onSuccessGet(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return Result.DELETE;
        }
        return result;
    }

    private Result onSuccessList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        int page = params.getInt("page");
        byte[] data = httpResult.getByteArray();

        PaymentDispatch.list(context, page, new JsonArray(data), false, transaction.isSync(), false);
        StoredObject.put(context, App.getProfileId(), PSO_PAYMENT_LIST, page, data);

        return Result.CONTINUE;
    }

    private Result onSuccessGet(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long paymentId = params.getLong("paymentId");

        byte[] data = httpResult.getByteArray();

        PaymentDispatch.get(context, paymentId, new JsonObject(data), false, transaction.isSync());
        StoredObject.put(context, App.getProfileId(), PSO_PAYMENT, paymentId, data);

        return Result.CONTINUE;
    }

    @Override
    public Result onFail(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        result = super.onFail(context, result, transaction, httpResult, throwable);
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
        return result;
    }
}

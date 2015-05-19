package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentTransactionHandler extends WebTransactionHandler implements PaymentConstants {

    public static byte[] pList(int page) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("page", page);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pGet(long paymentId) {
        try {
            JsonObject obj = new JsonObject("action", "pGet");
            obj.put("paymentId", paymentId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            String action = obj.getString("action");

            switch (action) {
                case "pList":
                    return handleList(context, transaction, resultData, obj);
                case "pGet":
                    return handleGet(context, transaction, resultData, obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.ERROR;
        }
        return Result.REQUEUE;
    }


    private Result handleList(Context context, WebTransaction transaction, HttpResult resultData,
                              JsonObject params) throws ParseException {
        int page = params.getInt("page");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, PSO_PAYMENT_LIST, page, data);

        PaymentDataDispatch.list(context, page, new JsonArray(data), transaction.isSync());
        return Result.FINISH;
    }

    private Result handleGet(Context context, WebTransaction transaction, HttpResult resultData,
                             JsonObject params) throws ParseException {
        long paymentId = params.getLong("paymentId");

        byte[] data = resultData.getByteArray();

        StoredObject.put(context, PSO_PAYMENT, paymentId, data);

        PaymentDataDispatch.get(context, paymentId, new JsonObject(data), transaction.isSync());
        return Result.FINISH;
    }
}

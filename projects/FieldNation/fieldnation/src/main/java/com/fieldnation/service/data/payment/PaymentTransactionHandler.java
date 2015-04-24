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

    public static byte[] pGetAll(int page) {
        try {
            JsonObject obj = new JsonObject("page", page);
            obj.put("action", "getall");
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pPayment(long paymentId) {
        try {
            JsonObject obj = new JsonObject("paymentId", paymentId);
            obj.put("action", "get");
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
            if (action.equals("getall")) {
                return handleGetAll(context, transaction, resultData, obj);
            } else if (action.equals("get")) {
                return handleGet(context, transaction, resultData, obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.ERROR;
        }
        return Result.REQUEUE;
    }


    private Result handleGetAll(Context context, WebTransaction transaction, HttpResult resultData,
                                JsonObject params) throws ParseException {
        int page = params.getInt("page");
        byte[] data = resultData.getResultsAsByteArray();

        StoredObject.put(context, PSO_PAYMENT_GET_ALL, page, data);

        PaymentDataDispatch.allPage(context, page, new JsonArray(data), transaction.isSync());
        return Result.FINISH;
    }

    private Result handleGet(Context context, WebTransaction transaction, HttpResult resultData,
                             JsonObject params) throws ParseException {
        long paymentId = params.getLong("paymentId");

        byte[] data = resultData.getResultsAsByteArray();

        StoredObject.put(context, PSO_PAYMENT_GET, paymentId, data);

        PaymentDataDispatch.payment(context, paymentId, new JsonObject(data), transaction.isSync());
        return Result.FINISH;
    }
}

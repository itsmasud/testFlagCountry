package com.fieldnation.service.data.payment;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentTransactionHandler extends WebTransactionHandler implements PaymentConstants {

    public static byte[] generateGetAllParams(int page) {
        try {
            JsonObject obj = new JsonObject("page", page);
            obj.put("action", "getall");
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] generatePaymentParams(long paymentId) {
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
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject obj = new JsonObject(transaction.getHandlerParams());
            String action = obj.getString("action");
            if (action.equals("getall")) {
                int page = obj.getInt("page");
                byte[] data = resultData.getResultsAsByteArray();

                StoredObject so = StoredObject.put(context, PSO_PAYMENT_GET_ALL, page + "", data);

                Bundle bundle = new Bundle();
                bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL);
                bundle.putInt(PARAM_PAGE, page);
                bundle.putByteArray(PARAM_DATA, data);
                TopicService.dispatchEvent(context, TOPIC_ID_GET_ALL, bundle, true);
                listener.onComplete(transaction);
                return;
            } else if (action.equals("get")) {
                long paymentId = obj.getLong("paymentId");

                byte[] data = resultData.getResultsAsByteArray();

                StoredObject.put(context, PSO_PAYMENT_GET, paymentId + "", data);

                Bundle bundle = new Bundle();
                bundle.putString(PARAM_ACTION, PARAM_ACTION_PAYMENT);
                bundle.putLong(PARAM_ID, paymentId);
                bundle.putByteArray(PARAM_DATA, data);
                TopicService.dispatchEvent(context, TOPIC_ID_PAYMENT, bundle, true);
                listener.onComplete(transaction);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onError(transaction);
            return;
        }
        listener.requeue(transaction);
    }
}

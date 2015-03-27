package com.fieldnation.service.data.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentDataClient extends TopicClient implements PaymentConstants {
    private final String TAG = UniqueTag.makeTag("PaymentDataClient");

    public PaymentDataClient(Listener listener) {
        super(listener);
    }

    /*-********************************-*/
    /*-         Data Interface         -*/
    /*-********************************-*/
    public static void getAll(Context context, int page) {
        Intent intent = new Intent(context, PaymentDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_ALL);
        intent.putExtra(PARAM_PAGE, page);
        context.startService(intent);
    }

    public boolean registerGetAll() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_GET_ALL, TAG);
    }

    public static void getPayment(Context context, long paymentId) {
        Intent intent = new Intent(context, PaymentDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_PAYMENT);
        intent.putExtra(PARAM_ID, paymentId);
        context.startService(intent);
    }

    public boolean registerGetPayment() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_PAYMENT, TAG);
    }

    /*-*********************************-*/
    /*-         Service events          -*/
    /*-*********************************-*/

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (TOPIC_ID_GET_ALL.equals(topicId)) {
                preOnGetAll((Bundle) payload);
            } else if (TOPIC_ID_PAYMENT.equals(topicId)) {
                preOnPayment((Bundle) payload);
            }
        }

        private void preOnPayment(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Payment>() {

                @Override
                protected Payment doInBackground(Bundle... params) {
                    try {
                        Bundle bundle = params[0];
                        return Payment.fromJson(new JsonObject(bundle.getByteArray(PARAM_DATA)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Payment payment) {
                    onPayment(payment);
                }
            }.executeEx(payload);
        }

        public void onPayment(Payment payment) {
        }

        private void preOnGetAll(Bundle bundle) {
            new AsyncTaskEx<Bundle, Object, List<Payment>>() {
                private int page;

                @Override
                protected List<Payment> doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    page = bundle.getInt(PARAM_PAGE);
                    List<Payment> list = new LinkedList<Payment>();
                    try {
                        JsonArray ja = new JsonArray(bundle.getByteArray(PARAM_DATA));

                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Payment.fromJson(ja.getJsonObject(i)));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                    return list;
                }

                @Override
                protected void onPostExecute(List<Payment> payments) {
                    onGetAll(payments, page);
                }
            }.executeEx(bundle);
        }

        public void onGetAll(List<Payment> list, int page) {
        }
    }

}

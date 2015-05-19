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

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    // get all
    public static void list(Context context, int page) {
        list(context, page, false);
    }

    public static void list(Context context, int page, boolean isSync) {
        Intent intent = new Intent(context, PaymentDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subList() {
        return subList(false);
    }

    public boolean subList(boolean isSync) {
        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        return register(topicId, TAG);
    }

    // get payment
    public static void get(Context context, long paymentId) {
        get(context, paymentId, false);
    }

    public static void get(Context context, long paymentId, boolean isSync) {
        Intent intent = new Intent(context, PaymentDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_ID, paymentId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGet(long paymentId) {
        return subGet(paymentId, false);
    }

    public boolean subGet(long paymentId, boolean isSync) {
        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (paymentId > 0) {
            topicId += "/" + paymentId;
        }

        return register(topicId, TAG);
    }

    /*-*********************************-*/
    /*-         Service events          -*/
    /*-*********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_LIST)) {
                preOnList((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_GET)) {
                preOnGet((Bundle) payload);
            }
        }

        private void preOnGet(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Payment>() {
                @Override
                protected Payment doInBackground(Bundle... params) {
                    try {
                        Bundle bundle = params[0];
                        return Payment.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Payment payment) {
                    onGet(payment);
                }
            }.executeEx(payload);
        }

        public void onGet(Payment payment) {
        }

        private void preOnList(Bundle bundle) {
            new AsyncTaskEx<Bundle, Object, List<Payment>>() {
                private int page;

                @Override
                protected List<Payment> doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    page = bundle.getInt(PARAM_PAGE);
                    List<Payment> list = new LinkedList<>();
                    try {
                        JsonArray ja = bundle.getParcelable(PARAM_DATA_PARCELABLE);

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
                    onList(payments, page);
                }
            }.executeEx(bundle);
        }

        public void onList(List<Payment> list, int page) {
        }
    }

}

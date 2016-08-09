package com.fieldnation.service.data.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnpigeon.TopicClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentClient extends TopicClient implements PaymentConstants {
    private static final String STAG = "PaymentClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public PaymentClient(Listener listener) {
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
        Intent intent = new Intent(context, PaymentService.class);
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
        Intent intent = new Intent(context, PaymentService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_PAYMENT_ID, paymentId);
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
            if (payload.containsKey(PARAM_ERROR) && payload.getBoolean(PARAM_ERROR)) {
                onGet(payload.getLong(PARAM_PAYMENT_ID), null, true);
            } else {
                new AsyncTaskEx<Bundle, Object, Payment>() {
                    private long paymentId;

                    @Override
                    protected Payment doInBackground(Bundle... params) {
                        try {
                            Bundle bundle = params[0];
                            paymentId = bundle.getLong(PARAM_PAYMENT_ID);
                            return Payment.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Payment payment) {
                        onGet(paymentId, payment, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onGet(long paymentId, Payment payment, boolean failed) {
        }

        private void preOnList(Bundle bundle) {
            if (bundle.containsKey(PARAM_ERROR) && bundle.getBoolean(PARAM_ERROR)) {
                onList(bundle.getInt(PARAM_PAGE), null, true, true);
            } else {
                new AsyncTaskEx<Bundle, Object, List<Payment>>() {
                    private boolean _isCached = true;
                    private int _page = 0;

                    @Override
                    protected List<Payment> doInBackground(Bundle... params) {
                        Bundle bundle = params[0];
                        _page = bundle.getInt(PARAM_PAGE);
                        _isCached = bundle.getBoolean(PARAM_IS_CACHED);
                        List<Payment> list = new LinkedList<>();
                        try {
                            JsonArray ja = bundle.getParcelable(PARAM_DATA_PARCELABLE);

                            for (int i = 0; i < ja.size(); i++) {
                                list.add(Payment.fromJson(ja.getJsonObject(i)));
                            }

                            return list;
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<Payment> o) {
                        onList(_page, o, false, _isCached);
                    }
                }.executeEx(bundle);
            }
        }

        public void onList(int page, List<Payment> list, boolean failed, boolean isCached) {
        }
    }
}

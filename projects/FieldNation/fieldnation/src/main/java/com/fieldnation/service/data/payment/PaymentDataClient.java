package com.fieldnation.service.data.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

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
        
    }

    /*-*********************************-*/
    /*-         Service events          -*/
    /*-*********************************-*/

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {

        }
    }

}

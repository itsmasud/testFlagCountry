package com.fieldnation.service.data.payment;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PaymentDataDispatch implements PaymentConstants {

    public static void allPage(Context context, int page, JsonArray data) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        TopicService.dispatchEvent(context, TOPIC_ID_GET_ALL, bundle, true);

    }

    public static void payment(Context context, long paymentId, JsonObject data) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_PAYMENT);
        bundle.putLong(PARAM_ID, paymentId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        TopicService.dispatchEvent(context, TOPIC_ID_PAYMENT, bundle, true);
    }
}

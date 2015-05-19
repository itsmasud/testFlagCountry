package com.fieldnation.service.data.payment;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PaymentDispatch implements PaymentConstants {

    public static void list(Context context, int page, JsonArray data, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);

    }

    public static void get(Context context, long paymentId, JsonObject data, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET);
        bundle.putLong(PARAM_ID, paymentId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (paymentId > 0) {
            topicId += "/" + paymentId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}

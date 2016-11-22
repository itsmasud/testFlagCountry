package com.fieldnation.service.data.payment;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class PaymentDispatch implements PaymentConstants {

    public static void list(Context context, int page, JsonArray data, boolean failed, boolean isSync, boolean isCached) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);
        bundle.putBoolean(PARAM_IS_CACHED, isCached);
        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void get(Context context, long paymentId, JsonObject data, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET);
        bundle.putLong(PARAM_PAYMENT_ID, paymentId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (paymentId > 0) {
            topicId += "/" + paymentId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}

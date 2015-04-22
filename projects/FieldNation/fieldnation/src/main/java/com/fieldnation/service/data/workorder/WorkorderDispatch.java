package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/20/2015.
 */
public class WorkorderDispatch implements WorkorderDataConstants {

    public static void workorder(Context context, JsonObject workorder, long workorderId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorder);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS + "/" + workorderId, bundle, true);
    }

    public static void workorderList(Context context, JsonArray workorders, int page, String selector) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorders);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_LIST_SELECTOR, selector);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST + "/" + selector, bundle, true);
    }

    public static void bundle(Context context, JsonObject data, long bundleId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putLong(PARAM_ID, bundleId);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_BUNDLE + "/" + bundleId, bundle, true);
    }

    public static void checkIn(Context context, long workorderId, byte[] data) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_CHECKIN);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putByteArray(PARAM_DATA_BYTE_ARRAY, data);
        TopicService.dispatchEvent(context, PARAM_ACTION_CHECKIN, bundle, true);
    }

    public static void checkOut(Context context, long workorderId, byte[] data) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_CHECKOUT);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putByteArray(PARAM_DATA_BYTE_ARRAY, data);
        TopicService.dispatchEvent(context, PARAM_ACTION_CHECKOUT, bundle, true);
    }

    public static void signature(Context context, JsonObject signature, long workorderId, long signatureId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, signature);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, bundle, true);
    }

}

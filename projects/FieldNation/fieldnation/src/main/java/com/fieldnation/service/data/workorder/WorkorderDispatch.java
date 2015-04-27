package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicService;

import java.io.File;

/**
 * Created by Michael Carver on 4/20/2015.
 */
public class WorkorderDispatch implements WorkorderDataConstants {

    public static void workorder(Context context, JsonObject workorder, long workorderId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorder);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS + (isSync ? "/Sync" : ""), bundle, true);
    }

    public static void workorderList(Context context, JsonArray workorders, int page, String selector, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorders);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_LIST_SELECTOR, selector);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST + (isSync ? "/Sync" : ""), bundle, true);
    }

    public static void bundle(Context context, JsonObject data, long bundleId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putLong(PARAM_ID, bundleId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_BUNDLE + (isSync ? "/Sync" : ""), bundle, true);
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

    public static void signature(Context context, JsonObject signature, long workorderId, long signatureId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, signature);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + (isSync ? "/Sync" : ""), bundle, true);
    }

    public static void deliverableFile(Context context, long workorderId, long deliverableId, File file, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putSerializable(PARAM_FILE, file);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DOWNLOAD_DELIVERABLE + (isSync ? "/Sync" : "") + "/" + workorderId + "/" + deliverableId, bundle, true);
    }

    public static void deliverable(Context context, JsonObject obj, long workorderId, long deliverableId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DELIVERABLE);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, obj);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DELIVERABLE + (isSync ? "/Sync" : "") + "/" + workorderId + "/" + deliverableId, bundle, true);
    }

}

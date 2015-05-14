package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

import java.io.File;

/**
 * Created by Michael Carver on 4/20/2015.
 */
public class WorkorderDispatch implements WorkorderConstants {

    public static void get(Context context, JsonObject workorder, long workorderId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorder);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS
                + (isSync ? "-SYNC" : "") + "/" + workorderId, bundle, Sticky.TEMP);
    }

    public static void list(Context context, JsonArray workorders, int page, String selector, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorders);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_LIST_SELECTOR, selector);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST
                + (isSync ? "-SYNC" : "")
                + "/" + selector, bundle, Sticky.TEMP);
    }

    public static void listMessages(Context context, long workorderId, JsonArray messages, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, messages);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST_MESSAGES
                + (isSync ? "/SYNC" : "")
                + "/" + workorderId, bundle, Sticky.TEMP);
    }

    public static void listAlerts(Context context, long workorderId, JsonArray alerts, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, alerts);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST_NOTIFICATIONS
                + (isSync ? "/SYNC" : "")
                + "/" + workorderId, bundle, Sticky.TEMP);
    }

    public static void listTasks(Context context, long workorderId, JsonArray tasks, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, tasks);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_TASKS);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context, PARAM_ACTION_LIST_TASKS
                + (isSync ? "/SYNC" : "")
                + "/" + workorderId, bundle, Sticky.TEMP);
    }

    public static void bundle(Context context, JsonObject data, long bundleId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putLong(PARAM_ID, bundleId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_BUNDLE
                + (isSync ? "-SYNC" : ""), bundle, Sticky.TEMP);
    }

    public static void signature(Context context, JsonObject signature, long workorderId, long signatureId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, signature);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE
                + (isSync ? "-SYNC" : ""), bundle, Sticky.TEMP);
    }

    public static void deliverableFile(Context context, long workorderId, long deliverableId, File file, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putSerializable(PARAM_FILE, file);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DOWNLOAD_DELIVERABLE
                + (isSync ? "-SYNC" : "") + "/" + workorderId + "/" + deliverableId, bundle, Sticky.TEMP);
    }

    public static void deliverable(Context context, JsonObject obj, long workorderId, long deliverableId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DELIVERABLE);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, obj);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DELIVERABLE
                + (isSync ? "-SYNC" : "") + "/" + workorderId + "/" + deliverableId, bundle, Sticky.TEMP);
    }

    public static void deliverableList(Context context, JsonArray obj, long workorderId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DELIVERABLE_LIST);
        bundle.putLong(PARAM_ID, workorderId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, obj);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, PARAM_ACTION_DELIVERABLE_LIST
                + (isSync ? "-SYNC" : ""), bundle, Sticky.TEMP);
    }

    public static void action(Context context, long workorderId, String action) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, action);
        bundle.putLong(PARAM_ID, workorderId);
        TopicService.dispatchEvent(context,
                PARAM_ACTION + "/" + workorderId,
                bundle, Sticky.TEMP);
    }
}

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
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorder);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "-_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void list(Context context, JsonArray workorders, int page, String selector, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, workorders);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_LIST_SELECTOR, selector);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (selector != null) {
            topicId += "/" + selector;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listMessages(Context context, long workorderId, JsonArray messages, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, messages);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        String topicId = TOPIC_ID_LIST_MESSAGES;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listAlerts(Context context, long workorderId, JsonArray alerts, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, alerts);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        String topicId = TOPIC_ID_LIST_ALERTS;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listTasks(Context context, long workorderId, JsonArray tasks, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_TASKS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, tasks);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        String topicId = TOPIC_ID_LIST_TASKS;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void bundle(Context context, JsonObject data, long bundleId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putLong(PARAM_WORKORDER_ID, bundleId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_GET_BUNDLE;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void signature(Context context, JsonObject signature, long workorderId, long signatureId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, signature);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_GET_SIGNATURE;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;

            if (signatureId > 0) {
                topicId += "/" + signatureId;
            }
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void downloadDeliverable(Context context, long workorderId, long deliverableId, File file, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putSerializable(PARAM_FILE, file);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_DOWNLOAD_DELIVERABLE;

        if (isSync) {
            topicId += "_SYNC";
        }

        topicId += "/" + workorderId + "/" + deliverableId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void getDeliverable(Context context, JsonObject obj, long workorderId, long deliverableId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_DELIVERABLE);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putLong(PARAM_DELIVERABLE_ID, deliverableId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, obj);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_GET_DELIVERABLE;

        if (isSync) {
            topicId += "_SYNC";
        }

        topicId += "/" + workorderId + "/" + deliverableId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listDeliverables(Context context, JsonArray obj, long workorderId, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_DELIVERABLES);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, obj);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_LIST_DELIVERABLES;

        if (isSync) {
            topicId += "_SYNC";
        }

        topicId += "/" + workorderId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void action(Context context, long workorderId, String action) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_COMPLETE);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        String topicId = TOPIC_ID_ACTION_COMPLETE;
        topicId += "/" + workorderId;

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}

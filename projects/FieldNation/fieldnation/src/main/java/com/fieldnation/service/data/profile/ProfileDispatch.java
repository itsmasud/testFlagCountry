package com.fieldnation.service.data.profile;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class ProfileDispatch implements ProfileConstants {

    public static void get(Context context, long profileId, JsonObject data, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_PROFILE_ID, profileId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.FOREVER);
    }

    public static void listNotifications(Context context, JsonArray data, int page, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String topicId = TOPIC_ID_NOTIFICATION_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listMessages(Context context, JsonArray data, int page, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String topicId = TOPIC_ID_MESSAGE_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void action(Context context, long profileId, String action, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, action);
        bundle.putLong(PARAM_PROFILE_ID, profileId);
        bundle.putBoolean(PARAM_ERROR, failed);

        String topicId = TOPIC_ID_ACTION_COMPLETE;

        if (profileId > 0) {
            topicId += "/" + profileId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}
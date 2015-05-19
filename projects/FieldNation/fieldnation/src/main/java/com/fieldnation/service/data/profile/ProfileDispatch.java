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

    public static void get(Context context, long profileId, JsonObject data, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_PROFILE_ID, profileId);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.FOREVER);
    }

    public static void listNotifications(Context context, JsonArray data, int page, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_NOTIFICATION_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void listMessages(Context context, JsonArray data, int page, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        String topicId = TOPIC_ID_MESSAGE_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }

    public static void action(Context context, long profileId, String action) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, action);
        bundle.putLong(PARAM_PROFILE_ID, profileId);

        String topicId = TOPIC_ID_ACTION_COMPLETE;

        if (profileId > 0) {
            topicId += "/" + profileId;
        }

        TopicService.dispatchEvent(context, topicId, bundle, Sticky.TEMP);
    }
}
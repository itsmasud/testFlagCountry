package com.fieldnation.service.data.profile;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class ProfileDispatch implements ProfileConstants {

    public static void myUserInformation(Context context, JsonObject data, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, TOPIC_ID_HAVE_PROFILE
                + (isSync ? "-SYNC" : ""), bundle, true);
    }

    public static void allNotifications(Context context, JsonArray data, int page, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL_NOTIFICATIONS);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, TOPIC_ID_ALL_NOTIFICATION_LIST
                + (isSync ? "-SYNC" : ""), bundle, false);

    }

    public static void allMessages(Context context, JsonArray data, int page, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_DATA_PARCELABLE, data);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL_MESSAGES);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        TopicService.dispatchEvent(context, TOPIC_ID_ALL_MESSAGES_LIST
                + (isSync ? "-SYNC" : ""), bundle, false);
    }
}

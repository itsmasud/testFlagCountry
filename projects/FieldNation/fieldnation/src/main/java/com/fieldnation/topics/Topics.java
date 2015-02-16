package com.fieldnation.topics;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.utils.misc;


/**
 * Created by michael.carver on 12/18/2014.
 */
public class Topics {
    private static final String TAG = "topics.Topics";

    public static final String TOPIC_SHUTDOWN = "TOPIC_SHUTDOWN";
    public static final String TOPIC_NETWORK_DOWN = "TOPIC_NETWORK_DOWN";
    public static final String TOPIC_NETWORK_UP = "TOPIC_NETWORK_UP";
    public static final String TOPIC_NEED_UPDATE = "TOPIC_NEED_UPDATE";


    public static void dispatchShutdown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_SHUTDOWN, null, false);
    }

    public static void dispatchNetworkUp(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NETWORK_UP, null, true);
    }

    public static void dispatchNetworkDown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NETWORK_DOWN, null, true);
    }

    public static void dispatchNeedUpdate(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NEED_UPDATE, null, true);
    }

    public static final String TOPIC_FILE_UPLOAD = "TOPIC_FILE_UPLOAD";
    public static final String TOPIC_FILE_UPLOAD_PARAM_URL = "TOPIC_FILE_UPLOAD_PARAM_URL";
    public static final String TOPIC_FILE_UPLOAD_PARAM_FILENAME = "TOPIC_FILE_UPLOAD_PARAM_FILENAME";
    public static final String TOPIC_FILE_UPLOAD_PARAM_STATE = "TOPIC_FILE_UPLOAD_PARAM_STATE";
    public static final String TOPIC_FILE_UPLOAD_PARAM_STATE_START = "TOPIC_FILE_UPLOAD_PARAM_STATE_START";
    public static final String TOPIC_FILE_UPLOAD_PARAM_STATE_FINISH = "TOPIC_FILE_UPLOAD_PARAM_STATE_FINISH";
    public static final String TOPIC_FILE_UPLOAD_PARAM_STATE_ERROR = "TOPIC_FILE_UPLOAD_PARAM_STATE_ERROR";
    public static final String TOPIC_FILE_UPLOAD_PARAM_ERROR_MESSAGE = "TOPIC_FILE_UPLOAD_PARAM_ERROR_MESSAGE";


    public static void dispatchFileUploadStart(Context context, String url, String filename) {
        Log.v(TAG, "dispatchFileUploadStart");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_URL, url);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_FILENAME, filename);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_STATE, TOPIC_FILE_UPLOAD_PARAM_STATE_START);

        TopicService.dispatchTopic(context, TOPIC_FILE_UPLOAD, bundle);
    }

    public static void dispatchFileUploadFinish(Context context, String url, String filename) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_URL, url);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_FILENAME, filename);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_STATE, TOPIC_FILE_UPLOAD_PARAM_STATE_FINISH);

        TopicService.dispatchTopic(context, TOPIC_FILE_UPLOAD, bundle);
    }

    public static void dispatchFileUploadError(Context context, String url, String filename, String errorMessage) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_URL, url);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_FILENAME, filename);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_STATE, TOPIC_FILE_UPLOAD_PARAM_STATE_ERROR);
        bundle.putString(TOPIC_FILE_UPLOAD_PARAM_ERROR_MESSAGE, errorMessage);

        TopicService.dispatchTopic(context, TOPIC_FILE_UPLOAD, bundle, false);
    }

    public static void subscribeFileUpload(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_FILE_UPLOAD, topicReceiver);
    }

    public static final String TOPIC_PROFILE_INVALIDATED = "TOPIC_PROFILE_INVALIDATED";
    public static final String TOPIC_PROFILE_UPDATE = "TOPIC_PROFILE_UPDATE";
    public static final String TOPIC_PROFILE_PARAM_PROFILE = "TOPIC_PROFILE_PARAM_PROFILE";

    public static void subscribeProfileInvalidated(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_PROFILE_INVALIDATED, topicReceiver);
    }

    public static void subscrubeProfileUpdated(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_PROFILE_UPDATE, topicReceiver);
    }

    public static void dispatchProfileInvalid(Context context) {
        if (context == null)
            return;

        TopicService.dispatchTopic(context, TOPIC_PROFILE_INVALIDATED, null, false);
    }

    public static void dispatchProfileUpdated(Context context, Profile profile) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putParcelable(TOPIC_PROFILE_PARAM_PROFILE, profile);

        TopicService.dispatchTopic(context, TOPIC_PROFILE_UPDATE, bundle, true);
    }
}

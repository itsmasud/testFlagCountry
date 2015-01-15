package com.fieldnation.topics;

import android.content.Context;
import android.os.Bundle;


/**
 * Created by michael.carver on 12/18/2014.
 */
public class Topics {
    public static final String TOPIC_SHUTDOWN = "TOPIC_SHUTDOWN";
    public static final String TOPIC_NETWORK_DOWN = "TOPIC_NETWORK_DOWN";
    public static final String TOPIC_NETWORK_UP = "TOPIC_NETWORK_UP";


    public static void dispatchShutdown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_SHUTDOWN, null, false);
    }

    public static void dispatchNetworkUp(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NETWORK_UP, null, true);
    }

    public static void dispatchNetworkDown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NETWORK_DOWN, null, true);
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

        TopicService.dispatchTopic(context, TOPIC_FILE_UPLOAD, bundle);
    }

    public static void subscribeFileUpload(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_FILE_UPLOAD, topicReceiver);
    }

    public static final String TOPIC_GA_EVENT = "TOPIC_GA_EVENT";
    public static final String TOPIC_GA_EVENT_PARAM_CATEGORY = "TOPIC_GA_EVENT_PARAM_CATEGORY";
    public static final String TOPIC_GA_EVENT_PARAM_ACTION = "TOPIC_GA_EVENT_PARAM_ACTION";
    public static final String TOPIC_GA_EVENT_PARAM_LABEL = "TOPIC_GA_EVENT_PARAM_LABEL";
    public static final String TOPIC_GA_EVENT_PARAM_VALUE = "TOPIC_GA_EVENT_PARAM_VALUE";

    public static final String GA_ACTION_LONG_CLICK = "LongClick";
    public static final String GA_ACTION_REQUEST_WORK = "RequestWork";
    public static final String GA_ACTION_COUNTER = "CounterOffer";
    public static final String GA_ACTION_CONFIRM_ASSIGN = "ConfirmAssign";
    public static final String GA_ACTION_CHECKIN = "Checkin";
    public static final String GA_ACTION_CHECKOUT = "Checkout";
    public static final String GA_ACTION_COMPLETE_WORK = "CompleteWork";
    public static final String GA_ACTION_START_MAP = "ViewLocation";


/*
    public static void dispatchGaEvent(Context context, String category, String action, String label) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_GA_EVENT_PARAM_CATEGORY, category);
        bundle.putString(TOPIC_GA_EVENT_PARAM_ACTION, action);
        bundle.putString(TOPIC_GA_EVENT_PARAM_LABEL, label);

        TopicService.dispatchTopic(context, TOPIC_GA_EVENT, bundle, false);
    }
*/

    public static void dispatchGaEvent(Context context, String category, String action, String label, long value) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_GA_EVENT_PARAM_CATEGORY, category);
        bundle.putString(TOPIC_GA_EVENT_PARAM_ACTION, action);
        bundle.putString(TOPIC_GA_EVENT_PARAM_LABEL, label);
        bundle.putLong(TOPIC_GA_EVENT_PARAM_VALUE, value);

        TopicService.dispatchTopic(context, TOPIC_GA_EVENT, bundle, false);
    }

    public static void subscribeGaEvent(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_GA_EVENT, topicReceiver);

    }

    public static final String TOPIC_GA_SCREENVIEW = "TOPIC_GA_SCREENVIEW";
    public static final String TOPIC_GA_SCREENVIEW_PARAM_NAME = "TOPIC_GA_SCREENVIEW_PARAM_NAME";

    public static void subscribeGaScreenView(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TOPIC_GA_SCREENVIEW, topicReceiver);
    }

    public static void dispatchGaScreenView(Context context, String screenName) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_GA_SCREENVIEW_PARAM_NAME, screenName);

        TopicService.dispatchTopic(context, TOPIC_GA_SCREENVIEW, bundle, false);
    }
}

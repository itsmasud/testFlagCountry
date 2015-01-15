package com.fieldnation.topics;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Michael Carver on 1/15/2015.
 */
public class GaTopic {
    public static final String EVENT = "TOPIC_GA_EVENT";
    public static final String EVENT_PARAM_CATEGORY = "TOPIC_GA_EVENT_PARAM_CATEGORY";
    public static final String EVENT_PARAM_ACTION = "TOPIC_GA_EVENT_PARAM_ACTION";
    public static final String EVENT_PARAM_LABEL = "TOPIC_GA_EVENT_PARAM_LABEL";
    public static final String EVENT_PARAM_VALUE = "TOPIC_GA_EVENT_PARAM_VALUE";

    public static final String ACTION_LONG_CLICK = "LongClick";
    public static final String ACTION_REQUEST_WORK = "RequestWork";
    public static final String ACTION_COUNTER = "CounterOffer";
    public static final String ACTION_CONFIRM_ASSIGN = "ConfirmAssign";
    public static final String ACTION_CHECKIN = "Checkin";
    public static final String ACTION_CHECKOUT = "Checkout";
    public static final String ACTION_COMPLETE_WORK = "CompleteWork";
    public static final String ACTION_START_MAP = "ViewLocation";


/*
    public static void dispatchEvent(Context context, String category, String action, String label) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_GA_EVENT_PARAM_CATEGORY, category);
        bundle.putString(TOPIC_GA_EVENT_PARAM_ACTION, action);
        bundle.putString(TOPIC_GA_EVENT_PARAM_LABEL, label);

        TopicService.dispatchTopic(context, TOPIC_GA_EVENT, bundle, false);
    }
*/

    public static void dispatchEvent(Context context, String category, String action, String label, long value) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(EVENT_PARAM_CATEGORY, category);
        bundle.putString(EVENT_PARAM_ACTION, action);
        bundle.putString(EVENT_PARAM_LABEL, label);
        bundle.putLong(EVENT_PARAM_VALUE, value);

        TopicService.dispatchTopic(context, EVENT, bundle, false);
    }

    public static void subscribeEvent(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, EVENT, topicReceiver);

    }

    public static final String SCREENVIEW = "TOPIC_GA_SCREENVIEW";
    public static final String SCREENVIEW_PARAM_NAME = "TOPIC_GA_SCREENVIEW_PARAM_NAME";

    public static void subscribeScreenView(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, SCREENVIEW, topicReceiver);
    }

    public static void dispatchScreenView(Context context, String screenName) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(SCREENVIEW_PARAM_NAME, screenName);

        TopicService.dispatchTopic(context, SCREENVIEW, bundle, false);
    }

    public static final String TIMING = "TOPIC_GA_TIMING";
    public static final String TIMING_PARAM_CATEGORY = "TOPIC_GA_TIMING_PARAM_CATEGORY";
    public static final String TIMING_PARAM_VARIABLE = "TOPIC_GA_TIMING_PARAM_VARIABLE";
    public static final String TIMING_PARAM_LABEL = "TOPIC_GA_TIMING_PARAM_LABEL";
    public static final String TIMING_PARAM_VALUE = "TOPIC_GA_TIMING_PARAM_VALUE";

    public static void subscribeTiming(Context context, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, 0, tag, TIMING, topicReceiver);
    }

    public static void dispatchTiming(Context context, String category, String variable, String label, long duration) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();

        if (category != null)
            bundle.putString(TIMING_PARAM_CATEGORY, category);
        if (variable != null)
            bundle.putString(TIMING_PARAM_VARIABLE, variable);
        if (label != null)
            bundle.putString(TIMING_PARAM_LABEL, label);

        bundle.putLong(TIMING_PARAM_VALUE, duration);

        TopicService.dispatchTopic(context, TIMING, bundle, false);
    }
}

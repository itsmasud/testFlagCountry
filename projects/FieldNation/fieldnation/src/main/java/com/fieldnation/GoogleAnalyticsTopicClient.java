package com.fieldnation;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;


/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GoogleAnalyticsTopicClient extends TopicClient {
    private final String TAG = UniqueTag.makeTag("GoogleAnalyticsTopicClient");

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public GoogleAnalyticsTopicClient(GoogleAnalyticsTopicClient.Listener listener) {
        super(listener);
    }

    /*-*****************************-*/
    /*-         GA Events           -*/
    /*-*****************************-*/
    private static final String EVENT = "TOPIC_GA_EVENT";
    private static final String EVENT_PARAM_CATEGORY = "TOPIC_GA_EVENT_PARAM_CATEGORY";
    private static final String EVENT_PARAM_ACTION = "TOPIC_GA_EVENT_PARAM_ACTION";
    private static final String EVENT_PARAM_LABEL = "TOPIC_GA_EVENT_PARAM_LABEL";
    private static final String EVENT_PARAM_VALUE = "TOPIC_GA_EVENT_PARAM_VALUE";

    public enum EventAction {
        LONG_CLICK("LongClick"),
        REQUEST_WORK("RequestWork"),
        COUNTER("CounterOffer"),
        CONFIRM_ASSIGN("ConfirmAssign"),
        CHECKIN("Checkin"),
        CHECKOUT("Checkout"),
        COMPLETE_WORK("CompleteWork"),
        START_MAP("ViewLocation"),
        COMPLETE_FN_EARNED("CompleteWorkFnEarned"),
        COMPLETE_FN_EARNED_GROSS("CompleteWorkFnEarnedGross"),
        CLICK("ActionClick");

        private final String _actionName;

        EventAction(String actionName) {
            _actionName = actionName;
        }

        public String getActionName() {
            return _actionName;
        }
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void dispatchEvent(Context context, String category, EventAction action, String label, long value) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(EVENT_PARAM_CATEGORY, category);
        bundle.putString(EVENT_PARAM_ACTION, action.getActionName());
        bundle.putString(EVENT_PARAM_LABEL, label);
        bundle.putLong(EVENT_PARAM_VALUE, value);

        TopicService.dispatchEvent(context, EVENT, bundle, Sticky.NONE);
    }

    public boolean registerEvents() {
        return register(EVENT, TAG);
    }


    /*-*********************************-*/
    /*-         GA ScreenView           -*/
    /*-*********************************-*/
    private static final String SCREENVIEW = "TOPIC_GA_SCREENVIEW";
    private static final String SCREENVIEW_PARAM_NAME = "TOPIC_GA_SCREENVIEW_PARAM_NAME";

    public boolean registerScreenView() {
        return register(SCREENVIEW, TAG);
    }

    public static void dispatchScreenView(Context context, String screenName) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(SCREENVIEW_PARAM_NAME, screenName);

        TopicService.dispatchEvent(context, SCREENVIEW, bundle, Sticky.NONE);
    }

    /*-*****************************-*/
    /*-         GA Timing           -*/
    /*-*****************************-*/
    private static final String TIMING = "TOPIC_GA_TIMING";
    private static final String TIMING_PARAM_CATEGORY = "TOPIC_GA_TIMING_PARAM_CATEGORY";
    private static final String TIMING_PARAM_VARIABLE = "TOPIC_GA_TIMING_PARAM_VARIABLE";
    private static final String TIMING_PARAM_LABEL = "TOPIC_GA_TIMING_PARAM_LABEL";
    private static final String TIMING_PARAM_VALUE = "TOPIC_GA_TIMING_PARAM_VALUE";

    public boolean registerTiming() {
        return register(TIMING, TAG);
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

        TopicService.dispatchEvent(context, TIMING, bundle, Sticky.NONE);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (!(payload instanceof Bundle)) {
                return;
            }

            Bundle pl = (Bundle) payload;

            if (EVENT.equals(topicId)) {
                String category = pl.getString(EVENT_PARAM_CATEGORY);
                String action = pl.getString(EVENT_PARAM_ACTION);
                String label = pl.getString(EVENT_PARAM_LABEL);

                Long value = null;
                if (pl.containsKey(EVENT_PARAM_VALUE)) {
                    value = pl.getLong(EVENT_PARAM_VALUE);
                }

                onGaEvent(category, action, label, value);

            } else if (SCREENVIEW.equals(topicId)) {
                String screenName = pl.getString(SCREENVIEW_PARAM_NAME);

                onGaScreen(screenName);

            } else if (GoogleAnalyticsTopicClient.TIMING.equals(topicId)) {
                String category = null;
                String variable = null;
                String label = null;
                Long value = null;

                if (pl.containsKey(TIMING_PARAM_CATEGORY))
                    category = pl.getString(TIMING_PARAM_CATEGORY);

                if (pl.containsKey(TIMING_PARAM_LABEL))
                    label = pl.getString(EVENT_PARAM_LABEL);

                if (pl.containsKey(TIMING_PARAM_VARIABLE))
                    variable = pl.getString(TIMING_PARAM_VARIABLE);

                if (pl.containsKey(TIMING_PARAM_VALUE))
                    value = pl.getLong(TIMING_PARAM_VALUE);

                onGaTiming(category, variable, label, value);
            }
        }

        public void onGaEvent(String category, String action, String label, Long value) {
        }

        public void onGaScreen(String screenName) {
        }

        public void onGaTiming(String category, String variable, String label, Long value) {
        }
    }
}

package com.fieldnation.topics;

import android.content.Context;


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
        TopicService.dispatchTopic(context, TOPIC_NETWORK_UP, null, false);
    }

    public static void dispatchNetworkDown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_NETWORK_DOWN, null, false);
    }
}

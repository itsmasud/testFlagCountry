package com.fieldnation.topics;

import android.content.Context;


/**
 * Created by michael.carver on 12/18/2014.
 */
public class Topics {
    public static final String TOPIC_SHUTDOWN = "TOPIC_SHUTDOWN";


    public static void dispatchShutdown(Context context) {
        TopicService.dispatchTopic(context, TOPIC_SHUTDOWN, null, false);
    }
}

package com.fieldnation.service.topics;

/**
 * Created by Michael Carver on 2/27/2015.
 */
interface TopicConstants {
    int WHAT_REGISTER_LISTENER = 1;
    int WHAT_UNREGISTER_LISTENER = 2;
    int WHAT_DISPATCH_EVENT = 3;
    int WHAT_DELETE_CLIENT = 4;

    String PARAM_TOPIC_ID = "PARAM_TOPIC_ID";
    String PARAM_USER_TAG = "PARAM_USER_TAG";
    String PARAM_TOPIC_PARCELABLE = "PARAM_TOPIC_PARCELABLE";
    String PARAM_STICKY = "PARAM_STICKY";
}

package com.fieldnation.service.topics;

/**
 * Created by Michael Carver on 2/27/2015.
 */
interface TopicConstants {
    public static final int WHAT_REGISTER_LISTENER = 1;
    public static final int WHAT_UNREGISTER_LISTENER = 2;
    public static final int WHAT_DISPATCH_EVENT = 3;
    public static final int WHAT_DELETE_CLIENT = 4;

    public static final String PARAM_TOPIC_ID = "PARAM_TOPIC_ID";
    public static final String PARAM_USER_TAG = "PARAM_USER_TAG";
    public static final String PARAM_TOPIC_PARCELABLE = "PARAM_TOPIC_PARCELABLE";
    public static final String PARAM_KEEP_LAST = "PARAM_KEEP_LAST";
}

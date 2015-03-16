package com.fieldnation.service.auth;

/**
 * Created by Michael Carver on 3/5/2015.
 */
public interface AuthTopicConstants {

    // Topics
    public static final String TOPIC_AUTH_STATE = "AuthTopicConstants:TOPIC_AUTH_STATE";
    public static final String TOPIC_AUTH_COMMAND = "AuthTopicConstants:TOPIC_AUTH_COMMAND";
    public static final String TOPIC_AUTH_STARTUP = "AuthTopicConstants:TOPIC_AUTH_STARTUP";

    // Params
    public static final String BUNDLE_PARAM_AUTH_TOKEN = "BUNDLE_PARAM_AUTH_TOKEN";

    // Types
    public static final String BUNDLE_PARAM_TYPE = "BUNDLE_PARAM_TYPE";
    public static final String BUNDLE_PARAM_TYPE_REQUEST = "BUNDLE_PARAM_TYPE_REQUEST";
    public static final String BUNDLE_PARAM_TYPE_INVALID = "BUNDLE_PARAM_TYPE_INVALID";
    public static final String BUNDLE_PARAM_TYPE_FAILED = "BUNDLE_PARAM_TYPE_FAILED";
    public static final String BUNDLE_PARAM_TYPE_COMPLETE = "BUNDLE_PARAM_TYPE_COMPLETE";
    public static final String BUNDLE_PARAM_TYPE_REMOVE = "BUNDLE_PARAM_TYPE_REMOVE";
    public static final String BUNDLE_PARAM_TYPE_CANCELLED = "BUNDLE_PARAM_TYPE_CANCELLED";
    public static final String BUNDLE_PARAM_TYPE_NO_NETWORK = "BUNDLE_PARAM_TYPE_NO_NETWORK";
    public static final String BUNDLE_PARAM_TYPE_NEED_PASSWORD = "BUNDLE_PARAM_TYPE_NEED_PASSWORD";



}

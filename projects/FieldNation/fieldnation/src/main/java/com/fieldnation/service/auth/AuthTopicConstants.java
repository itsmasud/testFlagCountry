package com.fieldnation.service.auth;

/**
 * Created by Michael Carver on 3/5/2015.
 */
public interface AuthTopicConstants {
    static final String TOPIC_AUTH_STATE = "AuthTopicConstants:TOPIC_AUTH_STATE";
    static final String PARAM_STATE = "AuthTopicConstants:PARAM_STATE";
    static final String PARAM_OAUTH = "AuthTopicConstants:PARAM_OAUTH";

    static final String TOPIC_AUTH_COMMAND_REQUEST = "AuthTopicConstants:TOPIC_AUTH_COMMAND_REQUEST";
    static final String TOPIC_AUTH_COMMAND_INVALIDATE = "AuthTopicConstants:TOPIC_AUTH_COMMAND_INVALIDATE";
    static final String TOPIC_AUTH_COMMAND_REMOVE = "AuthTopicConstants:TOPIC_AUTH_COMMAND_REMOVE";
    static final String TOPIC_AUTH_COMMAND_ADDED_ACCOUNT = "AuthTopicConstants:TOPIC_AUTH_COMMAND_ADDED_ACCOUNT";
}

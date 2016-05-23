package com.fieldnation.service.auth;

/**
 * Created by Michael Carver on 3/5/2015.
 */
public interface AuthTopicConstants {
    String TOPIC_AUTH_STATE = "AuthTopicConstants:TOPIC_AUTH_STATE";
    String PARAM_STATE = "AuthTopicConstants:PARAM_STATE";
    String PARAM_OAUTH = "AuthTopicConstants:PARAM_OAUTH";
    String PARAM_AUTHENTICATOR_RESPONSE = "AuthTopicConstants:PARAM_AUTHENTICATOR_RESPONSE";

    String TOPIC_AUTH_COMMAND_REQUEST = "AuthTopicConstants:TOPIC_AUTH_COMMAND_REQUEST";
    String TOPIC_AUTH_COMMAND_INVALIDATE = "AuthTopicConstants:TOPIC_AUTH_COMMAND_INVALIDATE";
    String TOPIC_AUTH_COMMAND_REMOVE = "AuthTopicConstants:TOPIC_AUTH_COMMAND_REMOVE";
    String TOPIC_AUTH_COMMAND_ADDED_ACCOUNT = "AuthTopicConstants:TOPIC_AUTH_COMMAND_ADDED_ACCOUNT";
    String TOPIC_AUTH_COMMAND_NEED_PASSWORD = "AuthTopicConstants:TOPIC_AUTH_COMMAND_NEED_PASSWORD";
}

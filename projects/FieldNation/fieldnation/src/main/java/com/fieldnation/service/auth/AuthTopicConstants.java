package com.fieldnation.service.auth;

/**
 * Created by Michael Carver on 3/5/2015.
 */
public interface AuthTopicConstants {
    String PARAM_STATE = "AuthTopicConstants:PARAM_STATE";
    String PARAM_OAUTH = "AuthTopicConstants:PARAM_OAUTH";
    String PARAM_AUTHENTICATOR_RESPONSE = "AuthTopicConstants:PARAM_AUTHENTICATOR_RESPONSE";

    String ADDRESS_AUTH_STATE = "AuthTopicConstants:ADDRESS_AUTH_STATE";
    String ADDRESS_AUTH_COMMAND_REQUEST = "AuthTopicConstants:ADDRESS_AUTH_COMMAND_REQUEST";
    String ADDRESS_AUTH_COMMAND_INVALIDATE = "AuthTopicConstants:ADDRESS_AUTH_COMMAND_INVALIDATE";
    String ADDRESS_AUTH_COMMAND_REMOVE = "AuthTopicConstants:ADDRESS_AUTH_COMMAND_REMOVE";
    String ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT = "AuthTopicConstants:ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT";
    String ADDRESS_AUTH_COMMAND_NEED_PASSWORD = "AuthTopicConstants:ADDRESS_AUTH_COMMAND_NEED_PASSWORD";
}

package com.fieldnation;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public interface GlobalTopicConstants {
    String TOPIC_ID_APP_UPDATE = "GlobalTopicConstants:TOPIC_ID_APP_UPDATE";
    String TOPIC_ID_GOT_PROFILE = "GlobalTopicConstants:TOPIC_ID_GOT_PROFILE";
    String TOPIC_ID_PROFILE_INVALID = "GlobalTopicConstants:TOPIC_ID_PROFILE_INVALID";
    String TOPIC_ID_SHUTDOWN = "GlobalTopicConstants:TOPIC_ID_SHUTDOWN";
    String TOPIC_ID_NETWORK_STATE = "GlobalTopicConstants:TOPIC_ID_NETWORK_STATE";
    String TOPIC_ID_NETWORK_COMMAND_CONNECT = "GlobalTopicConstants:TOPIC_ID_NETWORK_COMMAND_CONNECT";
    String TOPIC_ID_GCM_MESSAGE = "GlobalTopicConstants:TOPIC_ID_GCM_MESSAGE";
    String TOPIC_ID_SHOW_CONTACT_US = "GlobalTopicConstants:TOPIC_ID_SHOW_CONTACT_US";
    String TOPIC_ID_USER_SWITCHED = "GlobalTopicConstants:TOPIC_ID_USER_SWITCHED";
    String TOPIC_ID_SHOW_LOADING = "GlobalTopicConstants:TOPIC_ID_SHOW_LOADING";

    String PARAM_NETWORK_STATE = "PARAM_NETWORK_STATE";
    String PARAM_PROFILE = "PARAM_PROFILE";
    String PARAM_IS_LOADING = "PARAM_IS_LOADING";
    String PARAM_CONTACT_US_SOURCE = "PARAM_CONTACT_US_SOURCE";
    int NETWORK_STATE_CONNECTED = 1;
    int NETWORK_STATE_DISCONNECTED = 2;
    int NETWORK_STATE_CONNECTING = 3;

    String PARAM_GCM_MESSAGE = "PARAM_GCM_MESSAGE";


}

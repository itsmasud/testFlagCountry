package com.fieldnation;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public interface GlobalTopicConstants {
    String TOPIC_APP_UPDATE = "GlobalTopicConstants:TOPIC_APP_UPDATE";
    String TOPIC_GOT_PROFILE = "GlobalTopicConstants:TOPIC_GOT_PROFILE";
    String TOPIC_PROFILE_INVALID = "GlobalTopicConstants:TOPIC_PROFILE_INVALID";
    String TOPIC_SHUTDOWN = "GlobalTopicConstants:TOPIC_SHUTDOWN";
    String TOPIC_NETWORK_STATE = "GlobalTopicConstants:TOPIC_NETWORK_STATE";
    String TOPIC_NETWORK_COMMAND_CONNECT = "GlobalTopicConstants:TOPIC_NETWORK_COMMAND_CONNECT";
    String TOPIC_GCM_MESSAGE = "GlobalTopicConstants:TOPIC_GCM_MESSAGE";

    String PARAM_NETWORK_STATE = "PARAM_NETWORK_STATE";
    int NETWORK_STATE_CONNECTED = 1;
    int NETWORK_STATE_DISCONNECTED = 2;
    int NETWORK_STATE_CONNECTING = 3;

    String PARAM_GCM_MESSAGE = "PARAM_GCM_MESSAGE";

}

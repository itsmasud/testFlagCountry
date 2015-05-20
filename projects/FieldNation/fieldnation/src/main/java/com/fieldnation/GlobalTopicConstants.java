package com.fieldnation;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public interface GlobalTopicConstants {
    String TOPIC_APP_UPDATE = "GlobalTopicClient:TOPIC_APP_UPDATE";
    String TOPIC_GOT_PROFILE = "GlobalTopicClient:TOPIC_GOT_PROFILE";
    String TOPIC_PROFILE_INVALID = "GlobalTopicClient:TOPIC_PROFILE_INVALID";
    String TOPIC_SHUTDOWN = "GlobalTopicClient:TOPIC_SHUTDOWN";
    String TOPIC_NETWORK_STATE = "GlobalTopicClient:TOPIC_NETWORK_STATE";
    String TOPIC_NETWORK_COMMAND_CONNECT = "GlobalTopicClient:TOPIC_NETWORK_COMMAND_CONNECT";

    String PARAM_NETWORK_STATE = "NETWORK_STATE";
    int NETWORK_STATE_CONNECTED = 1;
    int NETWORK_STATE_DISCONNECTED = 2;
    int NETWORK_STATE_CONNECTING = 3;

}

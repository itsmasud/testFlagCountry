package com.fieldnation;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public interface AppMessagingConstants {
    String ADDRESS_APP_UPDATE = "AppMessagingConstants:ADDRESS_APP_UPDATE";
    String ADDRESS_GOT_PROFILE = "AppMessagingConstants:ADDRESS_GOT_PROFILE";
    String ADDRESS_PROFILE_INVALID = "AppMessagingConstants:ADDRESS_PROFILE_INVALID";
    String ADDRESS_SHUTDOWN = "AppMessagingConstants:ADDRESS_SHUTDOWN";
    String ADDRESS_FINISH_ACTIVITY = "AppMessagingConstants:ADDRESS_FINISH_ACTIVITY";
    String ADDRESS_NETWORK_STATE = "AppMessagingConstants:ADDRESS_NETWORK_STATE";
    String ADDRESS_NETWORK_COMMAND_CONNECT = "AppMessagingConstants:ADDRESS_NETWORK_COMMAND_CONNECT";
    String ADDRESS_GCM_MESSAGE = "AppMessagingConstants:ADDRESS_GCM_MESSAGE";
    String ADDRESS_USER_SWITCHED = "AppMessagingConstants:ADDRESS_USER_SWITCHED";
    String ADDRESS_SHOW_LOADING = "AppMessagingConstants:ADDRESS_SHOW_LOADING";

    String PARAM_NETWORK_STATE = "PARAM_NETWORK_STATE";
    String PARAM_PROFILE = "PARAM_PROFILE";
    String PARAM_IS_LOADING = "PARAM_IS_LOADING";
    String PARAM_CONTACT_US_SOURCE = "PARAM_CONTACT_US_SOURCE";
    int NETWORK_STATE_CONNECTED = 1;
    int NETWORK_STATE_DISCONNECTED = 2;
    int NETWORK_STATE_CONNECTING = 3;

    String PARAM_GCM_MESSAGE = "PARAM_GCM_MESSAGE";
}

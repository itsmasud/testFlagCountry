package com.fieldnation.service.data.profile;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public interface ProfileConstants {
    long CALL_BOUNCE_TIMER = 30000;

    String TOPIC_ID_HAVE_PROFILE = "ProfileDataService:TOPIC_ID_HAVE_PROFILE";
    String TOPIC_ID_ALL_NOTIFICATION_LIST = "ProfileDataService:TOPIC_ID_ALL_NOTIFICATION_LIST";
    String TOPIC_ID_ALL_MESSAGES_LIST = "ProfileDataService:TOPIC_ID_ALL_MESSAGES_LIST";
    String TOPIC_ID_ACTION_COMPLETE = "ProfileConstants:PARAM_ACTION_COMPLETE";

    String PARAM_ACTION = "ProfileConstants:PARAM_ACTION";
    String PARAM_ACTION_GET_MY_PROFILE = "PARAM_ACTION_GET_MY_PROFILE";
    String PARAM_ACTION_GET_ALL_NOTIFICATIONS = "PARAM_ACTION_GET_ALL_NOTIFICATIONS";
    String PARAM_ACTION_GET_ALL_MESSAGES = "PARAM_ACTION_GET_ALL_MESSAGES";

    String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";
    String PARAM_PAGE = "PAGE";

    String PARAM_IS_SYNC = "PARAM_IS_SYNC";

    String PARAM_PROFILE_ID = "PARAM_PROFILE_ID";

    String PSO_MESSAGE_PAGE = "MessagePage";
    String PSO_NOTIFICATION_PAGE = "NotificationPage";
    String PSO_PROFILE = "Profile";
    String PSO_MY_PROFILE_KEY = "Me";

}

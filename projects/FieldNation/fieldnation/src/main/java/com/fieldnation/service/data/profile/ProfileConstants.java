package com.fieldnation.service.data.profile;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public interface ProfileConstants {
    public static final long CALL_BOUNCE_TIMER = 30000;

    public static final String TOPIC_ID_HAVE_PROFILE = "ProfileDataService:TOPIC_ID_HAVE_PROFILE";
    public static final String TOPIC_ID_ALL_NOTIFICATION_LIST = "ProfileDataService:TOPIC_ID_ALL_NOTIFICATION_LIST";
    public static final String TOPIC_ID_ALL_MESSAGES_LIST = "ProfileDataService:TOPIC_ID_ALL_MESSAGES_LIST";
    public static final String PARAM_ACTION = "ProfileConstants:PARAM_ACTION";

    public static final String PARAM_ACTION_GET_MY_PROFILE = "PARAM_ACTION_GET_MY_PROFILE";
    public static final String PARAM_ACTION_GET_ALL_NOTIFICATIONS = "PARAM_ACTION_GET_ALL_NOTIFICATIONS";
    public static final String PARAM_ACTION_GET_ALL_MESSAGES = "PARAM_ACTION_GET_ALL_MESSAGES";

    public static final String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";
    public static final String PARAM_PAGE = "PAGE";

    public static final String PARAM_IS_SYNC = "PARAM_IS_SYNC";

    public static final String PARAM_PROFILE_ID = "PARAM_PROFILE_ID";

    public static final String PSO_MESSAGE_PAGE = "MessagePage";
    public static final String PSO_NOTIFICATION_PAGE = "NotificationPage";
    public static final String PSO_PROFILE = "Profile";
    public static final String PSO_MY_PROFILE_KEY = "Me";

}

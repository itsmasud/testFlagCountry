package com.fieldnation.service.data.help;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public interface HelpConstants {
    long CALL_BOUNCE_TIMER = 30000;


    /*
    Newly edited.
    Date: July 07, 2015
     */
    String TOPIC_ID_POST = "FeedbackDataService:TOPIC_ID_POST";

    /*
    ALL the below are from previous version.
    Previous edited Date: July 07, 2015
     */
    String TOPIC_ID_GET = "ProfileDataService:TOPIC_ID_GET";
    String TOPIC_ID_NOTIFICATION_LIST = "ProfileDataService:TOPIC_ID_NOTIFICATION_LIST";
    String TOPIC_ID_MESSAGE_LIST = "ProfileDataService:TOPIC_ID_MESSAGE_LIST";
    String TOPIC_ID_ACTION_COMPLETE = "ProfileConstants:TOPIC_ID_ACTION_COMPLETE";

    String PARAM_ACTION = "PARAM_ACTION";
    String PARAM_ACTION_GET = "PARAM_ACTION_GET";
    String PARAM_ACTION_LIST_NOTIFICATIONS = "PARAM_ACTION_LIST_NOTIFICATIONS";
    String PARAM_ACTION_LIST_MESSAGES = "PARAM_ACTION_LIST_MESSAGES";

    String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";
    String PARAM_PAGE = "PARAM_PAGE";
    String PARAM_IS_SYNC = "PARAM_IS_SYNC";
    String PARAM_PROFILE_ID = "PARAM_PROFILE_ID";
    String PARAM_ERROR = "PARAM_ERROR";

    String PSO_MESSAGE_PAGE = "MessagePage";
    String PSO_NOTIFICATION_PAGE = "NotificationPage";
    String PSO_PROFILE = "Profile";
}

package com.fieldnation.service.activityresult;

/**
 * Created by Michael on 7/8/2016.
 */
public interface ActivityResultConstants {
    String TOPIC_ID_START_ACTIVITY_FOR_RESULT = "ActivityResultConstants:TOPIC_ID_START_ACTIVITY_FOR_RESULT";
    String TOPIC_ID_START_ACTIVITY = "ActivityResultConstants:TOPIC_ID_START_ACTIVITY";
    String TOPIC_ID_ON_ACTIVITY_RESULT = "ActivityResultConstants:TOPIC_ID_ON_ACTIVITY_RESULT";

    String PARAM_INTENT = "PARAM_INTENT";
    String PARAM_REQUEST_CODE = "PARAM_REQUEST_CODE";
    String PARAM_RESULT_CODE = "PARAM_RESULT_CODE";

    int RESULT_CODE_ENABLE_GPS_CHECKIN = 1;
    int RESULT_CODE_ENABLE_GPS_CHECKOUT = 2;
    int RESULT_CODE_GET_ATTACHMENT = 3;
    int RESULT_CODE_GET_CAMERA_PIC = 4;
    int RESULT_CODE_SEND_EMAIL = 5;
    int RESULT_CODE_GET_SIGNATURE = 6;
}

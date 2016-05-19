package com.fieldnation.service.data.workorder;

/**
 * Created by Michael Carver on 3/24/2015.
 */
public interface WorkorderConstants {
    String TOPIC_ID_GET = "WorkorderConstants:TOPIC_ID_GET_WORKORDER";
    String TOPIC_ID_LIST = "WorkorderConstants:TOPIC_ID_LIST_WORKORDERS";
    String TOPIC_ID_ACTION_COMPLETE = "WorkorderConstants:TOPIC_ID_ACTION_COMPLETE";
    String TOPIC_ID_LIST_MESSAGES = "WorkorderConstants:TOPIC_ID_LIST_MESSAGES";
    String TOPIC_ID_LIST_ALERTS = "WorkorderConstants:TOPIC_ID_LIST_ALERTS";
    String TOPIC_ID_LIST_TASKS = "WorkorderConstants:TOPIC_ID_LIST_TASKS";
    String TOPIC_ID_GET_BUNDLE = "WorkorderConstants:TOPIC_ID_GET_BUNDLE";
    String TOPIC_ID_GET_SIGNATURE = "WorkorderConstants:TOPIC_ID_GET_SIGNATURE";
    String TOPIC_ID_DOWNLOAD_DELIVERABLE = "WorkorderConstants:TOPIC_ID_DOWNLOAD_DELIVERABLE";
    String TOPIC_ID_GET_DELIVERABLE = "WorkorderConstants:TOPIC_ID_GET_DELIVERABLE";
    String TOPIC_ID_UPLOAD_DELIVERABLE = "WorkorderConstants:TOPIC_ID_UPLOAD_DELIVERABLE";

    String PARAM_ACTION = "PARAM_ACTION";
    String PARAM_ACTION_COMPLETE = "PARAM_ACTION_COMPLETE";
    String PARAM_ACTION_LIST = "PARAM_ACTION_LIST";
    String PARAM_ACTION_GET = "PARAM_ACTION_GET";
    String PARAM_ACTION_LIST_MESSAGES = "PARAM_ACTION_LIST_MESSAGES";

    String PARAM_ACTION_GET_BUNDLE = "PARAM_ACTION_GET_BUNDLE";

    String PARAM_ACTION_GET_DELIVERABLE = "PARAM_ACTION_GET_DELIVERABLE";
    String PARAM_ACTION_UPLOAD_DELIVERABLE = "PARAM_ACTION_UPLOAD_DELIVERABLE";
    String PARAM_ACTION_CACHE_DELIVERABLE = "PARAM_ACTION_CACHE_DELIVERABLE";
    String PARAM_ACTION_DOWNLOAD_DELIVERABLE = "PARAM_ACTION_DOWNLOAD_DELIVERABLE";

    String PARAM_ACTION_LIST_NOTIFICATIONS = "PARAM_ACTION_LIST_NOTIFICATIONS";

    String PARAM_ACTION_LIST_TASKS = "PARAM_ACTION_LIST_TASKS";

    String PARAM_ACTION_GET_SIGNATURE = "PARAM_ACTION_GET_SIGNATURE";

    String PARAM_WORKORDER_ID = "PARAM_WORKORDER_ID";
    String PARAM_SIGNATURE_ID = "PARAM_SIGNATURE_ID";
    String PARAM_UPLOAD_SLOT_ID = "PARAM_UPLOAD_SLOT_ID";
    String PARAM_DELIVERABLE_ID = "PARAM_DELIVERABLE_ID";

    String PARAM_LIST_SELECTOR = "PARAM_LIST_SELECTOR";
    String PARAM_PAGE = "PARAM_PAGE";
    String PARAM_LOCAL_PATH = "PARAM_LOCAL_PATH";
    String PARAM_URI = "PARAM_URI";
    String PARAM_FILE_NAME = "PARAM_FILE_NAME";
    String PARAM_IS_SYNC = "PARAM_IS_SYNC";
    String PARAM_IS_CACHED = "PARAM_IS_CACHED";
    String PARAM_FILE = "PARAM_FILE";
    String PARAM_URL = "PARAM_URL";
    String PARAM_IS_COMPLETE = "PARAM_IS_COMPLETE";
    String PARAM_ALLOW_CACHE = "PARAM_ALLOW_CACHE";
    String PARAM_ERROR = "PARAM_ERROR";

    String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";

    String PSO_WORKORDER_LIST = "WorkorderList";
    String PSO_WORKORDER = "Workorder";
    String PSO_MESSAGE_LIST = "WorkorderMessageList";
    String PSO_ALERT_LIST = "WorkorderAlertList";
    String PSO_TASK_LIST = "WorkorderTaskList";
    String PSO_SIGNATURE = "Signature";
    String PSO_BUNDLE = "Bundle";

    String PSO_DELIVERABLE = "Deliverable";
    String PSO_DELIVERABLE_FILE = "DeliverableFile";
}

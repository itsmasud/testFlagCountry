package com.fieldnation.service.data.v2.workorder;

/**
 * Created by Michael on 7/21/2016.
 */
public interface WorkOrderConstants {
    // Topics
    String TOPIC_ID_SEARCH = "WorkOrderConstants:TOPIC_ID_SEARCH";
    String TOPIC_ID_ACTION_COMPLETE = "WorkOrderConstants:TOPIC_ID_ACTION_COMPLETE";

    // Parameters
    String PARAM_WORKORDER_ID = "PARAM_WORKORDER_ID";
    String PARAM_SEARCH_PARAMS = "PARAM_SEARCH_PARAMS";
    String PARAM_LIST_ENVELOPE = "PARAM_LIST_ENVELOPE";

    // Actions
    String PARAM_ACTION = "PARAM_ACTION";
    String ACTION_SEARCH = "ACTION_SEARCH";
    String ACTION_ACTION_COMPLETE = "ACTION_ACTION_COMPLETE";

    // Stored Object Names
    String PSO_WORKORDER = "WorkOrder";
    String PSO_WORKORDER_LIST = "WorkOrderList";

    // Params
    String PARAM_FAILED = "failed";
    String PARAM_WO_ACTION = "action";
}

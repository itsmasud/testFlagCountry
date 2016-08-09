package com.fieldnation.service.data.v2.workorder;

/**
 * Created by Michael on 8/8/2016.
 */
public enum WorkOrderListType {
    ASSIGNED("assigned"),
    AVAILABLE("available"),
    CANCELED("canceled"),
    COMPLETED("completed"),
    REQUESTED("requested"),
    ROUTED("routed");

    private final String param;

    WorkOrderListType(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

}

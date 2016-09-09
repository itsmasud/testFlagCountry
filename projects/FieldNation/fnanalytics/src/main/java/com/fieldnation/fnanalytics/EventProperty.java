package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/9/2016.
 */
public enum EventProperty {
    WORK_ORDER_ID("workorder_id");

    private String value;

    EventProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

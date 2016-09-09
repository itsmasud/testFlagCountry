package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/9/2016.
 */
public enum EventCategory {
    WORK_ORDER("Work Order"),
    COLLECT_SIGNATURE("Collect Signature");
    
    private String value;

    EventCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/9/2016.
 */
public enum EventLabel {
    COLLECT_SIGNATURE("Collect Signature"),
    NULL(null);

    private String value;

    EventLabel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/9/2016.
 */
public enum ScreenName {
    WORK_ORDER_DETAILS_WORK("Work Order Details - Work"),
    WORK_ORDER_DETAILS_MESSAGES("Work Order Details - Messages"),
    WORK_ORDER_DETAILS_ATTACHMENTS("Work Order Details - Attachments"),
    WORK_ORDER_DETAILS_ALERTS("Work Order Details - Alerts"),
    INBOX_MESSAGES("Inbox - Messages"),
    INBOX_ALERTS("Inbox - Alerts"),
    COLLECT_SIGNATURE("Collect Signature"),
    SIGNATURE_REJECT("Sign-off Rejected"),
    SIGNATURE_ACCEPT("Sign-off Accepted"),;

    private String value;

    ScreenName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

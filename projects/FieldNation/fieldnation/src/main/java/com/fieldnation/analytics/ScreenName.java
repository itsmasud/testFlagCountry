package com.fieldnation.analytics;

import com.fieldnation.fnanalytics.Screen;

/**
 * Created by Michael on 9/13/2016.
 */
public class ScreenName {
    private static final Screen WORK_ORDER_DETAILS_WORK = new Screen.Builder().name("Work Order Details - Work").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_DETAILS_MESSAGES = new Screen.Builder().name("Work Order Details - Messages").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_DETAILS_ATTACHMENTS = new Screen.Builder().name("Work Order Details - Attachments").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_DETAILS_ALERTS = new Screen.Builder().name("Work Order Details - Alerts").tag(SnowplowWrapper.TAG).build();
    private static final Screen INBOX_MESSAGES = new Screen.Builder().name("Inbox - Messages").tag(SnowplowWrapper.TAG).build();
    private static final Screen INBOX_ALERTS = new Screen.Builder().name("Inbox - Alerts").tag(SnowplowWrapper.TAG).build();
    private static final Screen COLLECT_SIGNATURE = new Screen.Builder().name("Collect Signature").tag(SnowplowWrapper.TAG).build();
    private static final Screen SIGNATURE_REJECT = new Screen.Builder().name("Sign-off Rejected").tag(SnowplowWrapper.TAG).build();
    private static final Screen SIGNATURE_ACCEPT = new Screen.Builder().name("Sign-off Accepted").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_CONTACT_US = new Screen.Builder().name("Additional Options - Contact Us").tag(SnowplowWrapper.TAG).build();

    // Not implemented
    private static final Screen WORK_ORDER_LIST_AVAILABLE = new Screen.Builder().name("Work Order List - Available").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_LIST_ASSIGNED = new Screen.Builder().name("Work Order List - Assigned").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_LIST_CANCELED = new Screen.Builder().name("Work Order List - Canceled").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_LIST_COMPLETED = new Screen.Builder().name("Work Order List - Completed").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_LIST_REQUESTED = new Screen.Builder().name("Work Order List - Requested").tag(SnowplowWrapper.TAG).build();
    private static final Screen WORK_ORDER_LIST_ROUTED = new Screen.Builder().name("Work Order List - Routed").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_PAYMENTS = new Screen.Builder().name("Additional Options - Payments").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_SETTINGS = new Screen.Builder().name("Additional Options - Settings").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_LOGOUT = new Screen.Builder().name("Additional Options - Logout").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_SEND_DEBUG = new Screen.Builder().name("Additional Options - Send Debug").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_LEGAL = new Screen.Builder().name("Additional Options - Legal").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS_APP_VERSION = new Screen.Builder().name("Additional Options - App Version").tag(SnowplowWrapper.TAG).build();

    public static Screen workOrderDetailsWork() {
        return WORK_ORDER_DETAILS_WORK;
    }

    public static Screen workOrderDetailsMessages() {
        return WORK_ORDER_DETAILS_MESSAGES;
    }

    public static Screen workOrderDetailsAttachments() {
        return WORK_ORDER_DETAILS_ATTACHMENTS;
    }

    public static Screen workOrderDetailsAlerts() {
        return WORK_ORDER_DETAILS_ALERTS;
    }

    public static Screen inboxMessages() {
        return INBOX_MESSAGES;
    }

    public static Screen inboxAlerts() {
        return INBOX_ALERTS;
    }

    public static Screen collectSignature() {
        return COLLECT_SIGNATURE;
    }

    public static Screen signatureReject() {
        return SIGNATURE_REJECT;
    }

    public static Screen signatureAccept() {
        return SIGNATURE_ACCEPT;
    }

    public static Screen contactUs() {
        return ADDITIONAL_OPTIONS_CONTACT_US;
    }

}

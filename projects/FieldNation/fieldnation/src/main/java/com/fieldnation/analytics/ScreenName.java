package com.fieldnation.analytics;

import com.fieldnation.fnanalytics.Screen;

/**
 * Created by Michael on 9/13/2016.
 */
public class ScreenName {
    private static final Screen WORK_ORDER_DETAILS = new Screen.Builder().name("Work Order Details").tag(SnowplowWrapper.TAG).build();
    private static final Screen SAVED_SEARCH = new Screen.Builder().name("Saved Search").tag(SnowplowWrapper.TAG).build();
    private static final Screen MESSAGES = new Screen.Builder().name("Messages").tag(SnowplowWrapper.TAG).build();
    private static final Screen NOTIFICATIONS = new Screen.Builder().name("Notifications").tag(SnowplowWrapper.TAG).build();
    private static final Screen SEARCH = new Screen.Builder().name("Search").tag(SnowplowWrapper.TAG).build();
    private static final Screen ADDITIONAL_OPTIONS = new Screen.Builder().name("Additional Options").tag(SnowplowWrapper.TAG).build();

    public static Screen workOrderDetails() {
        return WORK_ORDER_DETAILS;
    }

    public static Screen savedSearch() {
        return SAVED_SEARCH;
    }

    public static Screen messages() {
        return MESSAGES;
    }

    public static Screen notifications() {
        return NOTIFICATIONS;
    }

    public static Screen search() {
        return SEARCH;
    }

    public static Screen additionalOptions() {
        return ADDITIONAL_OPTIONS;
    }

}

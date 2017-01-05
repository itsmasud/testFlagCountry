package com.fieldnation.analytics;

import com.fieldnation.fnanalytics.Screen;

/**
 * Created by Michael on 9/13/2016.
 */
public class ScreenName {
    private static final Screen WORK_ORDER_DETAILS = new Screen.Builder().name("Work Order Details").tag(SnowplowWrapper.TAG).build();

    public static Screen workOrderDetails() {
        return WORK_ORDER_DETAILS;
    }

}

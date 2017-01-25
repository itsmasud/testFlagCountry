package com.fieldnation.analytics.trackers;

import android.content.Context;

/**
 * Created by mc on 1/10/17.
 */

public class TestTrackers {

    public static void runTests(Context context) {
        AdditionalOptionsTracker.test(context);
        InboxTracker.test(context);
        SavedSearchTracker.test(context);
        SearchTracker.test(context);
        WorkOrderTracker.test(context);
    }

}

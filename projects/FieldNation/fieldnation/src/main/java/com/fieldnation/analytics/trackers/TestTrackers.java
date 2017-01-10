package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.trackers.AdditionalOptionsTracker;
import com.fieldnation.analytics.trackers.InboxTracker;
import com.fieldnation.analytics.trackers.SavedSearchTracker;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.analytics.trackers.WorkOrderTracker;

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

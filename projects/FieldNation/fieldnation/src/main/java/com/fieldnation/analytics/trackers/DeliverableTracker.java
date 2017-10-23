package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 10/20/17.
 */

public class DeliverableTracker {
    private static final String TAG = "DeliverableTracker";
    // System = stage
    // Source uri?
    // local uri
    // uuid

    public enum Action {
        START, INFO, COMPLETE, FAIL,;
    }

    public enum Location {
        RECEIVER_ACTIVITY_MULTIPLE,
        RECEIVER_ACTIVITY_SINGLE,
        FILE_CACHE_CLIENT,
        ATTACHMENT_HELPER,
        WORKORDER_WEB_API,
        WEB_TRANSACTION_QUEUE,
    }

    public static void onEvent(Context context, UUIDGroup uuid, Action action, Location location) {
        Log.v(TAG, uuid + " " + action.toString() + " " + location.toString());
    }
}

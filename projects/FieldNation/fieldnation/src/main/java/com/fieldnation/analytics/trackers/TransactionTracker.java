package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 10/20/17.
 */

public class TransactionTracker {
    private static final String TAG = "TransactionTracker";

    public enum Action {
        START, INFO, COMPLETE, FAIL,;
    }

    public enum Location {
        WEB_TRANSACTION_SYSTEM_QUEUE,
    }

    public static void onEvent(Context context, String uuid, Action action, Location location) {
        Log.v(TAG, uuid + " " + action.toString() + " " + location.toString());
    }
}
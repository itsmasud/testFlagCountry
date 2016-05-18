package com.fieldnation.service.tracker;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerClient implements UploadTrackerConstants {

    public static void uploadQueued(Context context) {
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_UPLOAD);
        context.startService(intent);
    }

    public static void uploadSuccess(Context context) {
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_SUCCESS);
        context.startService(intent);
    }

    public static void uploadFailed(Context context, long workorderId) {
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_FAILED);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        context.startService(intent);
    }
}

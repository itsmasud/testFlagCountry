package com.fieldnation.service.tracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerClient implements UploadTrackerConstants {
    private static final String TAG = "UploadTrackerClient";

    public static void uploadQueued(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadQueued Debug Log")));
        Intent intent = new Intent(context, UploadTrackerService.class);
        intent.setAction(ACTION_QUEUED);
        intent.putExtra(UPLOAD_TYPE, trackerEnum.ordinal());
        context.startService(intent);
    }

    public static void uploadStarted(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadStarted Debug Log")));
        Intent intent = new Intent(context, UploadTrackerService.class);
        intent.setAction(ACTION_STARTED);
        intent.putExtra(UPLOAD_TYPE, trackerEnum.ordinal());
        context.startService(intent);
    }

    public static void uploadRequeued(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadRequeued Debug Log")));
        Intent intent = new Intent(context, UploadTrackerService.class);
        intent.setAction(ACTION_REQUEUED);
        intent.putExtra(UPLOAD_TYPE, trackerEnum.ordinal());
        context.startService(intent);
    }

    public static void uploadSuccess(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadSuccess Debug Log")));
        Intent intent = new Intent(context, UploadTrackerService.class);
        intent.setAction(ACTION_SUCCESS);
        intent.putExtra(UPLOAD_TYPE, trackerEnum.ordinal());
        context.startService(intent);
    }

    public static void uploadFailed(Context context, TrackerEnum trackerEnum, PendingIntent failedIntent) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Intent intent = new Intent(context, UploadTrackerService.class);
        intent.setAction(ACTION_FAILED);
        intent.putExtra(UPLOAD_TYPE, trackerEnum.ordinal());
        intent.putExtra(FAILED_PENDING_INTENT, failedIntent);
        context.startService(intent);
    }
}

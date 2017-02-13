package com.fieldnation.service.tracker;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerClient implements UploadTrackerConstants {
    private static final String TAG = "UploadTrackerClient";

    public static void uploadQueued(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadQueued Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_QUEUED);
        context.startService(intent);
    }

    public static void uploadStarted(Context context, String uploadType) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadStarted Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_STARTED);
        intent.putExtra(uploadType, uploadType);
        context.startService(intent);
    }

    public static void uploadRequeued(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadRequeued Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_REQUEUED);
        context.startService(intent);
    }

    public static void uploadSuccess(Context context, String uploadType) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadSuccess Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_SUCCESS);
        intent.putExtra(uploadType, uploadType);
        context.startService(intent);
    }

    public static void uploadFailed(Context context, long workorderId) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_FAILED);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        context.startService(intent);
    }

    public static void uploadFailed(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_FAILED);
        context.startService(intent);
    }

}

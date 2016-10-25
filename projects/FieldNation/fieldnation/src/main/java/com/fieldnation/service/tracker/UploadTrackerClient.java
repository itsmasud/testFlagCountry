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

    public static void uploadStarted(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadStarted Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_STARTED);
        context.startService(intent);
    }

    public static void uploadProgress(Context context, long pos, long size, long time) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadProgress Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_PROGRESS);
        intent.putExtra(PARAM_POS, pos);
        intent.putExtra(PARAM_SIZE, size);
        intent.putExtra(PARAM_TIME, time);
        context.startService(intent);
    }

    public static void uploadRequeued(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadRequeued Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_REQUEUED);
        context.startService(intent);
    }

    public static void uploadSuccess(Context context) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadSuccess Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_SUCCESS);
        context.startService(intent);
    }

    public static void uploadFailed(Context context, long workorderId) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Intent intent = new Intent(context, UploadTracker.class);
        intent.setAction(ACTION_FAILED);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        context.startService(intent);
    }
}

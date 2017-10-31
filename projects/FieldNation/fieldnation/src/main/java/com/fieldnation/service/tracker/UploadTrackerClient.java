package com.fieldnation.service.tracker;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerClient implements UploadTrackerConstants {
    private static final String TAG = "UploadTrackerClient";

    public static void uploadQueued(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadQueued Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_QUEUED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadStarted(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadStarted Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_STARTED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadRequeued(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadRequeued Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_REQUEUED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadSuccess(Context context, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadSuccess Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_SUCCESS);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadFailed(Context context, TrackerEnum trackerEnum, PendingIntent failedIntent) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_FAILED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(FAILED_PENDING_INTENT, failedIntent);
        UploadTrackerService.getInstance().process(bundle);
    }
}

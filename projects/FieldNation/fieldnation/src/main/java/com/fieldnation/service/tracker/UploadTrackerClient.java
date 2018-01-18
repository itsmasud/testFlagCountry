package com.fieldnation.service.tracker;

import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerClient implements UploadTrackerConstants {
    private static final String TAG = "UploadTrackerClient";

    public static void uploadQueued(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadQueued Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_QUEUED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadStarted(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadStarted Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_STARTED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadDelete(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadDelete Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_DELETE);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadRetry(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadRetry Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_RETRY);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadSuccess(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadSuccess Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_SUCCESS);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }

    public static void uploadFailed(WebTransaction webTransaction, TrackerEnum trackerEnum) {
        Log.v(TAG, DebugUtils.getStackTrace(new Exception("uploadFailed Debug Log")));
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, ACTION_FAILED);
        bundle.putInt(UPLOAD_TYPE, trackerEnum.ordinal());
        bundle.putParcelable(TRANSACTION, webTransaction);
        UploadTrackerService.getInstance().process(bundle);
    }
}

package com.fieldnation.service.tracker;

import android.app.PendingIntent;
import android.os.Bundle;

import com.fieldnation.App;

import java.util.Hashtable;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerService implements UploadTrackerConstants {
    private static final String TAG = "UploadTracker";

    private Hashtable<TrackerEnum, UploadTracker> trackers = new Hashtable<>();

    private static UploadTrackerService _instance = null;

    private UploadTrackerService() {
        trackers.put(TrackerEnum.DELIVERABLES, new UploadTrackerDeliverables());
        trackers.put(TrackerEnum.PHOTOS, new UploadTrackerPhotos());
    }

    public static UploadTrackerService getInstance() {
        if (_instance == null)
            _instance = new UploadTrackerService();

        return _instance;
    }

    public void process(Bundle bundle) {

        if (bundle == null || !bundle.containsKey(UPLOAD_TYPE)) {
            return;
        }

        String action = bundle.getString(ACTION);
        TrackerEnum trackerEnum = TrackerEnum.values()[bundle.getInt(UPLOAD_TYPE)];
        PendingIntent failIntent = null;

        if (bundle.containsKey(FAILED_PENDING_INTENT))
            failIntent = bundle.getParcelable(FAILED_PENDING_INTENT);

        trackers.get(trackerEnum).update(App.get(), action, failIntent);
    }
}

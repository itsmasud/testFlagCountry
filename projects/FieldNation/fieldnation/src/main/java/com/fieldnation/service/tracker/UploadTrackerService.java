package com.fieldnation.service.tracker;

import android.app.PendingIntent;
import android.content.Intent;

import com.fieldnation.fntools.MultiThreadedService;

import java.util.Hashtable;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTrackerService extends MultiThreadedService implements UploadTrackerConstants {
    private static final String TAG = "UploadTracker";

    private Hashtable<TrackerEnum, UploadTracker> trackers = new Hashtable<>();

    @Override
    public void onCreate() {
        trackers.put(TrackerEnum.DELIVERABLES, new UploadTrackerDeliverables());
        trackers.put(TrackerEnum.PHOTOS, new UploadTrackerPhotos());
        super.onCreate();
    }

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {

        if (intent.getExtras() == null || !intent.getExtras().containsKey(UPLOAD_TYPE)) {
            return;
        }

        String action = intent.getAction();
        TrackerEnum trackerEnum = TrackerEnum.values()[intent.getExtras().getInt(UPLOAD_TYPE)];
        PendingIntent failIntent = null;

        if (intent.getExtras().containsKey(FAILED_PENDING_INTENT))
            failIntent = intent.getExtras().getParcelable(FAILED_PENDING_INTENT);

        trackers.get(trackerEnum).update(this, action, failIntent);
    }

    @Override
    public boolean isStillWorking() {
        for (UploadTracker tracker : trackers.values()) {
            if (tracker.isViable())
                return true;
        }
        return false;
    }
}

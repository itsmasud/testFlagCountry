package com.fieldnation.service.tracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 2/20/17.
 */

public class UploadTrackerDeliverables implements UploadTrackerConstants, UploadTracker {
    private static final String TAG = "UploadTrackerDeliverables";

    private int _notificationId = App.secureRandom.nextInt();
    private int _uploadQueued = 0;
    private int _uploadRunning = 0;
    private int _uploadSuccess = 0;
    private int _uploadFailed = 0;
    private long _resetTimer = 0;

    public UploadTrackerDeliverables() {
    }

    public void update(Context context, String action, PendingIntent failIntent) {
        if (resetTimerExpired()) {
            _uploadQueued = 0;
            _uploadRunning = 0;
            _uploadFailed = 0;
            _uploadSuccess = 0;
            _notificationId = App.secureRandom.nextInt();
            _resetTimer = 0;
        }

        Log.v(TAG, action);

        switch (action) {
            case ACTION_QUEUED:
                _uploadQueued++;
                break;
            case ACTION_STARTED:
                _uploadRunning++;
                _uploadQueued--;
                if (_uploadQueued < 0) _uploadQueued = 0;
                break;
            case ACTION_REQUEUED:
                _uploadRunning--;
                if (_uploadRunning < 0) _uploadRunning = 0;
                _uploadQueued++;
                break;
            case ACTION_SUCCESS:
                _uploadRunning--;
                if (_uploadRunning < 0) _uploadRunning = 0;
                _uploadSuccess++;
                break;
            case ACTION_FAILED:
                _uploadFailed++;
                _uploadRunning--;
                if (_uploadRunning < 0) _uploadRunning = 0;
                createFailedNotification(context, failIntent);
                break;
            default:
                break;
        }
        Log.v(TAG, "_uploadQueued: " + _uploadQueued + ", _uploadRunning: " + _uploadRunning
                + ", _uploadSuccess: " + _uploadSuccess + ", _uploadFailed: " + _uploadFailed);

        populateNotification(context);
    }

    public boolean resetTimerExpired() {
        return _resetTimer > 0 && _resetTimer <= System.currentTimeMillis();
    }

    public boolean isViable() {
        return _uploadQueued > 0 || _uploadRunning > 0 || _uploadFailed > 0 || _uploadSuccess > 0;
    }

    private void populateNotification(Context context) {
        // running
        if (_uploadRunning > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_anim_upload_start)
                    .setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_deliverables_uploading, _uploadRunning, _uploadRunning))
                    .setContentText(context.getResources().getQuantityString(
                            R.plurals.num_uploads_queued, _uploadQueued, _uploadQueued))
                    .setColor(context.getResources().getColor(R.color.fn_clickable_text));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notificationId, builder.build());

            // queued
        } else if (_uploadQueued > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_queued)
                    .setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_files_to_upload, _uploadQueued, _uploadQueued))
                    .setContentText(context.getResources().getQuantityString(
                            R.plurals.num_uploads_completed, _uploadSuccess, _uploadSuccess))
                    .setColor(context.getResources().getColor(R.color.fn_clickable_text));

            if (_uploadFailed > 0) {
                builder.setContentText(context.getString(R.string.num_failed, _uploadFailed));
            }

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notificationId, builder.build());

            // complete
        } else if (_uploadQueued == 0 && _uploadRunning == 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_success)
                    .setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_deliverables_uploaded, _uploadSuccess, _uploadSuccess))
                    .setContentText(context.getResources().getQuantityString(
                            R.plurals.num_uploads_failed, _uploadFailed, _uploadFailed))
                    .setColor(context.getResources().getColor(R.color.fn_accent_color));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notificationId, builder.build());

            _resetTimer = System.currentTimeMillis() + 10000;
        }
    }

    public void createFailedNotification(Context context, PendingIntent failIntent) {
        NotificationCompat.Builder
                builder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(R.drawable.ic_notif_fail)
                .setContentText("File upload has failed")
                .setContentIntent(failIntent)
                .setContentTitle(context.getString(R.string.failed))
                .setColor(context.getResources().getColor(R.color.fn_red));

        NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(App.secureRandom.nextInt(), builder.build());
    }
}

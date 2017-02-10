package com.fieldnation.service.tracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.MultiThreadedService;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTracker extends MultiThreadedService implements UploadTrackerConstants {
    private static final String TAG = "UploadTracker";

    private int _notifcationId = App.secureRandom.nextInt();
    private int _uploadQueued = 0;
    private int _uploadRunning = 0;
    private int _uploadSuccess = 0;
    private int _uploadFailed = 0;
    private long _resetTimer = 0;
    private String uploadType;

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        if (_resetTimer <= System.currentTimeMillis() && _resetTimer > 0) {
            _uploadQueued = 0;
            _uploadRunning = 0;
            _uploadFailed = 0;
            _uploadSuccess = 0;
            _notifcationId = App.secureRandom.nextInt();
            _resetTimer = 0;
        }

        String action = intent.getAction();

        if (intent.getExtras() != null && intent.getExtras().containsKey(PARAM_ACTION_PHOTO_UPLOAD)) {
            uploadType = intent.getExtras().getString(PARAM_ACTION_PHOTO_UPLOAD);
        } else if (intent.getExtras() != null && intent.getExtras().containsKey(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
            uploadType = intent.getExtras().getString(PARAM_ACTION_UPLOAD_DELIVERABLE);
        }

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
                createFailedNotification(intent.getLongExtra(PARAM_WORKORDER_ID, 0));
                break;
            default:
                break;
        }
        Log.v(TAG, "_uploadQueued: " + _uploadQueued + ", _uploadRunning: " + _uploadRunning
                + ", _uploadSuccess: " + _uploadSuccess + ", _uploadFailed: " + _uploadFailed);

        populateNotification();
    }

    @Override
    public boolean isStillWorking() {
        return _uploadQueued > 0 || _uploadRunning > 0 || _uploadFailed > 0 || _uploadSuccess > 0;
    }

    private void populateNotification() {
        // running
        if (_uploadRunning > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_anim_upload_start)
//                    .setContentTitle(getResources().getQuantityString(
//                            R.plurals.num_files_uploading, _uploadRunning, _uploadRunning))
                    .setContentText(getResources().getQuantityString(
                            R.plurals.num_uploads_queued, _uploadQueued, _uploadQueued))
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            if (uploadType != null && uploadType.equals(PARAM_ACTION_PHOTO_UPLOAD)) {
                builder.setContentTitle(getResources().getQuantityString(
                        R.plurals.num_deliverables_uploading, _uploadRunning, _uploadRunning));
            } else if (uploadType != null && uploadType.equals(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
                builder.setContentTitle(getResources().getQuantityString(
                        R.plurals.num_photos_uploading, _uploadRunning, _uploadRunning));
            }



            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            // queued
        } else if (_uploadQueued > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_queued)
                    .setContentTitle(getResources().getQuantityString(
                            R.plurals.num_files_to_upload, _uploadQueued, _uploadQueued))
                    .setContentText(getResources().getQuantityString(
                            R.plurals.num_uploads_completed, _uploadSuccess, _uploadSuccess))
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            if (_uploadFailed > 0) {
                builder.setContentText(getString(R.string.num_failed, _uploadFailed));
            }

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            // complete
        } else if (_uploadQueued == 0 && _uploadRunning == 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_success)
                    .setContentTitle(getResources().getQuantityString(
                            R.plurals.num_deliverables_uploaded, _uploadSuccess, _uploadSuccess))
                    .setContentText(getResources().getQuantityString(
                            R.plurals.num_uploads_failed, _uploadFailed, _uploadFailed))
                    .setColor(getResources().getColor(R.color.fn_accent_color));


            if (uploadType != null && uploadType.equals(PARAM_ACTION_PHOTO_UPLOAD)) {
                builder.setContentTitle(getResources().getQuantityString(
                        R.plurals.num_deliverables_uploaded, _uploadSuccess, _uploadSuccess));
            } else if (uploadType != null && uploadType.equals(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
                builder.setContentTitle(getResources().getQuantityString(
                        R.plurals.num_photos_uploaded, _uploadSuccess, _uploadSuccess));
            }

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            _resetTimer = System.currentTimeMillis() + 10000;
        }
    }

    private void createFailedNotification(long workorderId) {
        Intent workorderIntent = WorkorderActivity.makeIntentShow(App.get(), workorderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), workorderIntent, 0);

        NotificationCompat.Builder
                builder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(R.drawable.ic_notif_fail)
                .setContentTitle(getString(R.string.failed))
                .setColor(getResources().getColor(R.color.fn_red));

        if (workorderId != 0) {
            builder.setContentText(getString(R.string.wo_num_file_upload_has_failed, workorderId))
                    .setContentIntent(pendingIntent);
        } else {
            builder.setContentText("Profile photo upload has failed");
        }

        NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(App.secureRandom.nextInt(), builder.build());
    }
}

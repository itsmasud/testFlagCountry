package com.fieldnation.service.tracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.MSService;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTracker extends MSService implements UploadTrackerConstants {
    private static final String TAG = "UploadTracker";

    private int _notifcationId = App.secureRandom.nextInt();
    private int _uploadQueued = 0;
    private int _uploadRunning = 0;
    private int _uploadSuccess = 0;
    private int _uploadFailed = 0;
    private long _resetTimer = 0;

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
                    .setContentTitle(getResources().getQuantityString(R.plurals.num_deliverables_uploading, _uploadRunning))
                    //.setTicker(_uploadQueued + " Uploads queued")
                    .setContentText(getResources().getQuantityString(R.plurals.num_deliverables_uploading, _uploadQueued))
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            // queued
        } else if (_uploadQueued > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_queued)
                    .setContentTitle(getResources().getQuantityString(R.plurals.num_deliverables_to_upload, _uploadQueued))
                    //.setTicker(_uploadQueued + " Uploads queued")
                    .setContentText(getResources().getQuantityString(R.plurals.num_uploads_queued, _uploadQueued))
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            if (_uploadSuccess > 0) {
                builder //.setTicker(_uploadSuccess + " Uploads complete")
                        .setContentText(getResources().getQuantityString(R.plurals.num_uploads_completed, _uploadSuccess));
            } else if (_uploadFailed > 0) {
                builder //.setTicker(_uploadFailed + " Uploads failed")
                        .setContentText(_uploadFailed + " failed");
            }

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            // complete
        } else if (_uploadQueued == 0 && _uploadRunning == 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_success)
                    .setContentTitle(getResources().getQuantityString(R.plurals.num_deliverables_uploaded, _uploadSuccess))
                    //.setTicker(_uploadFailed + " Uploads failed")
                    .setContentText(getResources().getQuantityString(R.plurals.num_uploads_failed, _uploadFailed))
                    .setColor(getResources().getColor(R.color.fn_accent_color));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());

            _resetTimer = System.currentTimeMillis() + 10000;
        }
    }

    private void createFailedNotification(long workorderId) {
        Intent workorderIntent = WorkorderActivity.makeIntentShow(App.get(), workorderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), 0, workorderIntent, 0);

        NotificationCompat.Builder
                builder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(R.drawable.ic_notif_fail)
                .setContentTitle("Failed")
                //.setTicker("WO " + workorderId + " file upload has failed")
                .setContentText("WO " + workorderId + " file upload has failed")
                .setColor(getResources().getColor(R.color.fn_red))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(App.secureRandom.nextInt(), builder.build());
    }
}

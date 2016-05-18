package com.fieldnation.service.tracker;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.MSService;

/**
 * Created by Michael on 5/18/2016.
 */
public class UploadTracker extends MSService implements UploadTrackerConstants {

    private static final int _notifcationId = App.secureRandom.nextInt();

    private int _uploadCount = 0;
    private int _completeCount = 0;
    private int _successCount = 0;

    private boolean _isUploading;

    private long _lastUpload = 0;


    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_UPLOAD:
                actionUpload();
                break;
            case ACTION_SUCCESS:
                actionSuccess();
                break;
            case ACTION_FAILED:
                actionFailed(intent.getLongExtra(PARAM_WORKORDER_ID, 0));
                break;
            default:
                break;
        }
    }

    private void actionUpload() {

        // if not uploading, then start it up
        if (!_isUploading) {
            _uploadCount = 1;
            _completeCount = 0;
            _successCount = 0;
            _isUploading = true;
            _lastUpload = System.currentTimeMillis();
        } else {
            // if uploading then continue
            _uploadCount++;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                .setLargeIcon(null)
                .setSmallIcon(R.drawable.ic_notif_queued)
                .setContentTitle((_uploadCount - _completeCount) + " Deliverables to Upload")
                .setTicker(_completeCount + " uploads complete")
                .setContentText(_completeCount + " uploads complete")
                .setColor(getResources().getColor(R.color.fn_clickable_text));

        NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(_notifcationId, builder.build());
    }

    private void actionSuccess() {
        _completeCount++;
        _successCount++;

        if (_completeCount == _uploadCount) {
            _isUploading = false;
            // switch to success
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_success)
                    .setContentTitle("Success")
                    .setTicker(_successCount + " deliverables have been uploaded")
                    .setContentText(_successCount + " deliverables have been uploaded")
                    .setColor(getResources().getColor(R.color.fn_accent_color));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_queued)
                    .setContentTitle((_uploadCount - _completeCount) + " Deliverables to Upload")
                    .setTicker(_completeCount + " uploads complete")
                    .setContentText(_completeCount + " uploads complete")
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());
        }
    }

    private void actionFailed(long workorderId) {
        _completeCount++;
        if (_completeCount == _uploadCount) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                    .setLargeIcon(null)
                    .setSmallIcon(R.drawable.ic_notif_queued)
                    .setContentTitle((_uploadCount - _completeCount) + " Deliverables to Upload")
                    .setTicker(_completeCount + " uploads complete")
                    .setContentText(_completeCount + " uploads complete")
                    .setColor(getResources().getColor(R.color.fn_clickable_text));

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(_notifcationId, builder.build());
        } else {
            {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                        .setLargeIcon(null)
                        .setSmallIcon(R.drawable.ic_notif_queued)
                        .setContentTitle((_uploadCount - _completeCount) + " Deliverables to Upload")
                        .setTicker(_completeCount + " uploads complete")
                        .setContentText(_completeCount + " uploads complete")
                        .setColor(getResources().getColor(R.color.fn_clickable_text));

                NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
                manager.notify(_notifcationId, builder.build());
            }

            {
                // failed
                NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get())
                        .setLargeIcon(null)
                        .setSmallIcon(R.drawable.ic_notif_fail)
                        .setContentTitle("Failed")
                        .setTicker("WO file upload has failed")
                        .setContentText("WO file upload has failed")
                        .setColor(getResources().getColor(R.color.fn_red));

                NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
                manager.notify(App.secureRandom.nextInt(), builder.build());
            }
        }
    }

}

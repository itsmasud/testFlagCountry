package com.fieldnation.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fieldnation.App;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/13/2016.
 */
public class AnalyticsPassThroughService extends Service {
    private static final String TAG = "AnalyticsPassThroughService";

    private static final String PARAM_NOTIFICATION_ID = "notificationId";
    private static final String PARAM_PENDING_INTENT = "pendingIntent";
    private static final String PARAM_EVENT = "event";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        if (intent != null) {
            try {
                if (intent.hasExtra(PARAM_NOTIFICATION_ID)) {
                    NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    manager.cancel(intent.getIntExtra(PARAM_NOTIFICATION_ID, 0));
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            try {
                Tracker.event(this, (Event) intent.getParcelableExtra(PARAM_EVENT));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            try {
                ((PendingIntent) intent.getParcelableExtra(PARAM_PENDING_INTENT)).send(this, App.secureRandom.nextInt(), new Intent());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context, Event event, PendingIntent pendingIntent, int notificationId) {
        Intent retval = new Intent(context, AnalyticsPassThroughService.class);
        retval.setAction("DUMMY");
        retval.putExtra(PARAM_EVENT, event);
        retval.putExtra(PARAM_PENDING_INTENT, pendingIntent);
        retval.putExtra(PARAM_NOTIFICATION_ID, notificationId);
        return retval;
    }

    public static PendingIntent createPendingIntent(Context context, Event event, PendingIntent pendingIntent, int notificationId) {
        return PendingIntent.getService(context, App.secureRandom.nextInt(), createIntent(context, event, pendingIntent, notificationId), 0);

    }
}

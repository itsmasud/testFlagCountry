package com.fieldnation.service;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        if (intent != null) {
            try {
                Tracker.event(this, (Event) intent.getParcelableExtra("event"));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            try {
                ((PendingIntent) intent.getParcelableExtra("pendingIntent")).send(this, App.secureRandom.nextInt(), new Intent());
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

    public static Intent createIntent(Context context, Event event, PendingIntent pendingIntent) {
        Intent retval = new Intent(context, AnalyticsPassThroughService.class);
        retval.setAction("DUMMY");
        retval.putExtra("event", event);
        retval.putExtra("pendingIntent", pendingIntent);
        return retval;
    }

    public static PendingIntent createPendingIntent(Context context, Event event, PendingIntent pendingIntent) {
        return PendingIntent.getService(context, App.secureRandom.nextInt(), createIntent(context, event, pendingIntent), 0);

    }
}

package com.fieldnation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fieldnation.App;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.client.UsersWebApi;
import com.fieldnation.v2.data.model.Coords;

/**
 * Created by Michael on 9/29/2016.
 * <p>
 * This service will track the user for a specified amount of time, and can be stopped at any time
 */

public class GpsTrackingService extends Service {
    private static final String TAG = "GpsTrackingService";

    private static final long INTERVAL = 60000;
    private static final long FAST_INTERVAL = 30000;
    private static final SimpleGps.Priority PRIORITY = SimpleGps.Priority.CITY;

    private long _expirationTime = 0;
    private SimpleGps _simpleGps = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("STOP")) {
                if (_simpleGps != null) {
                    _simpleGps.stop();
                    _simpleGps = null;
                }
                _expirationTime = 0;
                stopSelf();

            } else if (intent.getAction().equals("START")) {
                long expires = intent.getLongExtra("EXPIRE", 0);
                if (expires > _expirationTime)
                    _expirationTime = expires;

                if (_simpleGps == null) {
                    _simpleGps = new SimpleGps(this)
                            .priority(PRIORITY)
                            .interval(INTERVAL)
                            .fastestInterval(FAST_INTERVAL)
                            .updateListener(_gps_listener)
                            .start(this);
                }
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (_simpleGps != null) {
            _simpleGps.stop();
            _simpleGps = null;
        }
        super.onDestroy();
    }

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            Log.v(TAG, location.toString());
            try {
                UsersWebApi.addCoords(App.get(), (int) App.getProfileId(), new Coords(location.getLatitude(), location.getLongitude()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            if (_expirationTime > System.currentTimeMillis()) {
                stopSelf();
            }
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
        }

        @Override
        public void onPermissionDenied(SimpleGps simpleGps) {
        }
    };

    public static void start(Context context, long expires) {
        Intent intent = new Intent(context, GpsTrackingService.class);
        intent.setAction("START");
        intent.putExtra("EXPIRE", expires);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, GpsTrackingService.class);
        intent.setAction("STOP");
        context.startService(intent);
    }
}

package com.fieldnation.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.fieldnation.App;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.MultiThreadedService;
import com.fieldnation.service.data.v2.profile.ProfileTransactionBuilder;

/**
 * Created by Michael on 9/29/2016.
 * <p>
 * This service will track the user for a specified amount of time, and can be stopped at any time
 */

public class GpsTrackingService extends MultiThreadedService {
    private static final String TAG = "GpsTrackingService";

    private static final long INTERVAL = 60000;
    private static final long FAST_INTERVAL = 30000;
    private static final SimpleGps.Priority PRIORITY = SimpleGps.Priority.CITY;

    private long _expirationTime = 0;
    private SimpleGps _simpleGps = null;

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public void processIntent(Intent intent) {
        if (intent == null)
            return;

        if (intent.getAction() == null)
            return;

        String action = intent.getAction();
        switch (action) {
            case "STOP":
                if (_simpleGps != null) {
                    _simpleGps.stop();
                    _simpleGps = null;
                }
                _expirationTime = 0;
                break;
            case "START":
                long expires = intent.getLongExtra("EXPIRE", 0);
                if (expires > _expirationTime)
                    _expirationTime = expires;
                if (_simpleGps == null)
                    _simpleGps = new SimpleGps(this)
                            .priority(PRIORITY)
                            .interval(INTERVAL)
                            .fastestInterval(FAST_INTERVAL)
                            .updateListener(_gps_listener)
                            .start(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isStillWorking() {
        if (_expirationTime > System.currentTimeMillis())
            return true;
        return super.isStillWorking();
    }

    @Override
    public void onDestroy() {
        if (_simpleGps != null) {
            _simpleGps.stop();
            _simpleGps = null;
        }
        super.onDestroy();
    }

    private SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            Log.v(TAG, location.toString());
            ProfileTransactionBuilder.geo(App.get(), location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            _expirationTime = 0;
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

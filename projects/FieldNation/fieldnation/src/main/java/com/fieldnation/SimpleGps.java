package com.fieldnation;

import android.content.Context;
import android.location.Location;

import java.util.Hashtable;

/**
 * Created by Michael on 6/23/2016.
 */
public class SimpleGps {
    private static final String TAG = "SimpleGps";
    private static final Hashtable<String, SimpleGps> _clients = new Hashtable<>();

    private GpsLocationService _gpsLocationService = null;
    private Listener _listener = null;
    private String _key = null;

    private SimpleGps(Context context) {
        Log.v(TAG, "SimpleGps - constucted");
        _gpsLocationService = new GpsLocationService(context);
        _gpsLocationService.setListener(_gpsListener);
        _key = context.getApplicationContext().toString();
        _clients.put(_key, this);
    }

    public static SimpleGps with(Context context) {
        if (!_clients.containsKey(context.getApplicationContext().toString())) {
            return new SimpleGps(context);
        }

        return _clients.get(context.getApplicationContext().toString());
    }

    public boolean isLocationEnabled() {
        return _gpsLocationService.isLocationServicesEnabled();
    }

    public SimpleGps start(Listener listener) {
        _listener = listener;
        _gpsLocationService.startLocation();
        return this;
    }

    public void stop() {
        if (_gpsLocationService != null && _gpsLocationService.isRunning())
            _gpsLocationService.stopLocationUpdates();

        _clients.remove(_key);
    }

    private final GpsLocationService.Listener _gpsListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "SimpleGps - _gpsListener");
            if (_listener != null)
                _listener.onLocation(location);
        }
    };

    public interface Listener {
        void onLocation(Location location);
    }
}

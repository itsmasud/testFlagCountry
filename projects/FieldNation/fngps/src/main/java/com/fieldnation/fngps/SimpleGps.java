package com.fieldnation.fngps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fntools.ContextProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Michael on 9/28/2016.
 */

public class SimpleGps {
    private static final String TAG = "SimpleGps";

    public static final long ONE_SEC = 1000;
    public static final long ONE_MIN = ONE_SEC * 60;

    // Gps Api
    private final FusedLocationProviderApi _providerApi = LocationServices.FusedLocationApi;
    private LocationRequest _locationRequest = LocationRequest.create();
    private LocationManager _locationManager;
    private GoogleApiClient _gglApiClient;

    // Data
    private boolean _isRunning = false;
    private Listener _listener = null;

    // Services
    private PermissionsClient _permissionsClient;

    // Constructors
    public SimpleGps(Context context) {
        Log.v(TAG, "new SimpleGps()");
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        _locationRequest.setInterval(30000);
        _locationRequest.setFastestInterval(5000);

        _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isLocationEnabled() {
        return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public SimpleGps updateListener(Listener updateListener) {
        _listener = updateListener;
        return this;
    }

    // Setup
    public enum Priority {
        HIGHEST(LocationRequest.PRIORITY_HIGH_ACCURACY),
        BLOCK(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
        CITY(LocationRequest.PRIORITY_LOW_POWER),
        NO_POWER(LocationRequest.PRIORITY_NO_POWER);

        private int _priority;

        Priority(int priority) {
            _priority = priority;
        }

        public int getPriority() {
            return _priority;
        }
    }

    public SimpleGps priority(Priority priority) {
        _locationRequest.setPriority(priority.getPriority());
        return this;
    }

    public SimpleGps interval(long millis) {
        _locationRequest.setInterval(millis);
        return this;
    }

    public SimpleGps maxWaitTime(long millis) {
        _locationRequest.setMaxWaitTime(millis);
        return this;
    }

    public SimpleGps fastestInterval(long millis) {
        _locationRequest.setFastestInterval(millis);
        return this;
    }

    public SimpleGps expirationDuration(long millis) {
        _locationRequest.setExpirationDuration(millis);
        return this;
    }

    public SimpleGps expirationTime(long millis) {
        _locationRequest.setExpirationTime(millis);
        return this;
    }

    public SimpleGps numUpdates(int numUpdates) {
        _locationRequest.setNumUpdates(numUpdates);
        return this;
    }

    public SimpleGps smallestDisplacement(float smallestDisplacementMeters) {
        _locationRequest.setSmallestDisplacement(smallestDisplacementMeters);
        return this;
    }

    // State control
    public boolean isRunning() {
        return _isRunning;
    }

    public SimpleGps start(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            Log.v(TAG, "Waiting for permission");
            _permissionsClient = new PermissionsClient(_permissionsListener);
            _permissionsClient.connect(ContextProvider.get());
            PermissionsClient.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION});

        } else {
            Log.v(TAG, "start");
            Log.v(TAG, _locationRequest.toString());
            try {
                _gglApiClient = new GoogleApiClient.Builder(context)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(_gglApi_callbacks)
                        .addOnConnectionFailedListener(_gglApi_failedCallbacks)
                        .build();

                _isRunning = true;
                _gglApiClient.connect();
            } catch (Exception ex) {
                Log.v(TAG, ex);
                if (_listener != null)
                    _listener.onFail(this);
            }
        }
        return this;
    }

    public SimpleGps stop() {
        Log.v(TAG, "stop");
        if (!_isRunning) {
            return this;
        }

        if (_permissionsClient != null) _permissionsClient.disconnect(ContextProvider.get());

        if (_gglApiClient.isConnected()) {
            _providerApi.removeLocationUpdates(_gglApiClient, _locationUpdate_listener);
            _gglApiClient.disconnect();
            _gglApiClient = null;
        }
        _isRunning = false;
        return this;
    }

    private final PermissionsClient.ResponseListener _permissionsListener = new PermissionsClient.ResponseListener() {
        @Override
        public PermissionsClient getClient() {
            return _permissionsClient;
        }

        @Override
        public void onComplete(String permission, int grantResult) {
            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    start(ContextProvider.get());
                } else {
                    if (_listener != null) _listener.onFail(SimpleGps.this);
                    stop();
                }
            }
        }
    };

    // API Listeners
    private final GoogleApiClient.ConnectionCallbacks _gglApi_callbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            if (!_isRunning)
                return;
            Log.v(TAG, "onConnected");
            try {
                _providerApi.requestLocationUpdates(_gglApiClient, _locationRequest, _locationUpdate_listener);

                Location location = _providerApi.getLastLocation(_gglApiClient);

                if (location != null && _listener != null) {
                    Log.v(TAG, location.toString());
                    _listener.onLocation(SimpleGps.this, location);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                if (_listener != null)
                    _listener.onFail(SimpleGps.this);
                stop();
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            if (!_isRunning)
                return;
            Log.v(TAG, "onConnectionSuspended");
            if (_listener != null)
                _listener.onFail(SimpleGps.this);
            stop();
        }
    };

    private final GoogleApiClient.OnConnectionFailedListener _gglApi_failedCallbacks = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            if (!_isRunning)
                return;
            Log.v(TAG, "onConnectionFailed");
            if (_listener != null)
                _listener.onFail(SimpleGps.this);
            stop();
        }
    };

    private final LocationListener _locationUpdate_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (!_isRunning)
                return;
            Log.v(TAG, "onLocationChanged");
            Log.v(TAG, location.toString());
            if (_listener != null)
                _listener.onLocation(SimpleGps.this, location);
        }
    };


    public interface Listener {
        void onLocation(SimpleGps simpleGps, Location location);

        void onFail(SimpleGps simpleGps);
    }
}

package com.fieldnation.fntools;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GpsLocationService {
    private static final String TAG = "GpsLocationService";

    private static final long INTERVAL = 1000 * 30; // 30 seconds
    private static final long FASTEST_INTERVAL = 1000 * 5; // 5 seconds

    private final LocationRequest _locationRequest;
    private final GoogleApiClient _googleApiClient;
    private final FusedLocationProviderApi _fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private final LocationManager _locationManager;
    private Location _location;
    private boolean _isRunning = false;
    private Listener _listener;

    public GpsLocationService(Context context) {
        // build a location request
        _locationRequest = LocationRequest.create();
        _locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        _locationRequest.setInterval(INTERVAL);
        _locationRequest.setFastestInterval(FASTEST_INTERVAL);

        _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        _googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(_connectionCallbacks)
                .addOnConnectionFailedListener(_connectionFailListener)
                .build();

        _isRunning = true;
        _googleApiClient.connect();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void startLocation() {
        try {
        if (!_googleApiClient.isConnected() && !_googleApiClient.isConnecting()) {
            _isRunning = true;
            _googleApiClient.connect();
        } else if (!_googleApiClient.isConnecting()) {
            _isRunning = true;
            _fusedLocationProviderApi.requestLocationUpdates(_googleApiClient, _locationRequest, _locationListener);
        }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            ToastClient.toast(App.get(), "Could not get gps location", Toast.LENGTH_SHORT);
            //if (_listener != null)
            //_listener.onLocationNotAllowed();
        }
    }

    public boolean hasLocation() {
        return _location != null;
    }

    public Location getLocation() {
        return _location;
    }

    public boolean isLocationServicesEnabled() {
        return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isRunning() {
        return _isRunning;
    }

    public void stopLocationUpdates() {
        Log.v(TAG, "stopLocationUpdates");
        if (_googleApiClient.isConnected())
            _fusedLocationProviderApi.removeLocationUpdates(_googleApiClient, _locationListener);
        _isRunning = false;
    }


    private final LocationListener _locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "LocationListener.onLocationChanged()");
            _location = location;
            if (_listener != null)
                _listener.onLocation(_location);
            stopLocationUpdates();
        }
    };

    private final GoogleApiClient.ConnectionCallbacks _connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            try {
                Log.v(TAG, "GoogleApiClient.ConnectionCallbacks.onConnected");
                Location currentLocation = _fusedLocationProviderApi.getLastLocation(_googleApiClient);

                // Have a location... this is good, grab it and shutdown
                if (currentLocation != null) {
                    Log.v(TAG, "GoogleApiClient.ConnectionCallbacks.onConnected Done");
                    _location = currentLocation;
                    if (_listener != null)
                        _listener.onLocation(_location);
                    stopLocationUpdates();
                } else {
                    // no location, request one
                    _fusedLocationProviderApi.requestLocationUpdates(_googleApiClient, _locationRequest, _locationListener);
                }
            } catch (SecurityException ex) {
                Log.v(TAG, ex);
                ToastClient.toast(App.get(), "Could not get gps location", Toast.LENGTH_SHORT);
                stopLocationUpdates();

            } catch (Exception ex) {
                Log.v(TAG, ex);
                stopLocationUpdates();
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.v(TAG, "onConnectionSuspended");
        }
    };

    private final GoogleApiClient.OnConnectionFailedListener _connectionFailListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.v(TAG, "onConnectionFailed");
            _isRunning = false;
        }
    };

    public interface Listener {
        void onLocation(Location location);
    }

}
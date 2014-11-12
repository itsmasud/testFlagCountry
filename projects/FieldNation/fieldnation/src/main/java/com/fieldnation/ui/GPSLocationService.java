package com.fieldnation.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GPSLocationService {

    private static final String TAG = "ui.GPSLocationService";

    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long ONE_MIN = 1000 * 60;
    private static final long REFRESH_TIME = ONE_MIN * 5;
    private static final float MINIMUM_ACCURACY = 50.0f;

    private Activity _locationActivity;
    private LocationRequest _locationRequest;
    private GoogleApiClient _googleApiClient;
    private Location _location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    public GPSLocationService(Activity locationActivity) {
        _locationRequest = LocationRequest.create();
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        _locationRequest.setInterval(INTERVAL);
        _locationRequest.setFastestInterval(FASTEST_INTERVAL);
        _locationActivity = locationActivity;

        _googleApiClient = new GoogleApiClient.Builder(locationActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(_connectionCallbacks)
                .addOnConnectionFailedListener(_connectionFailListener)
                .build();

        if (_googleApiClient != null) {
            _googleApiClient.connect();
        }
    }


    public Location getLocation() {
        return _location;
    }

    public boolean isLocationServiceEnabled() {
        LocationManager lm = (LocationManager)
                _locationActivity.getApplicationContext().getSystemService(_locationActivity.getApplicationContext().LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        return (!LocationManager.PASSIVE_PROVIDER.equals(provider));
    }

    public boolean isGpsEnabled() {
        LocationManager service = (LocationManager) _locationActivity.getApplicationContext().getSystemService(_locationActivity.getApplicationContext().LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER) && service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void turnOnGPS() {
        if (!isLocationServiceEnabled()) {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            _locationActivity.getApplicationContext().sendBroadcast(intent);
        }
    }

    public void turnOffGPS() {
        if (isGpsEnabled()) {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", false);
            _locationActivity.getApplicationContext().sendBroadcast(intent);
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_locationActivity.getApplicationContext());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, _locationActivity, 0).show();
            return false;
        }
    }

    /**
     * Function to show settings alert dialog
     */
    public void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialogBox = alertDialog.create();
        // Showing Alert Message
        alertDialogBox.show();
    }

    /**
     * Function to show settings alert dialog if failed
     */
    public void showSettingsOffAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Location service settings");

        // Setting Dialog Message
        alertDialog.setMessage("Location service is not disabled. Do you want to go to settings menu, disabled it.?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private LocationListener _locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //the current location accuracy is greater than existing accuracy
            //then store the current location
            if (_location == null || _location.getAccuracy() < location.getAccuracy()) {
                _location = location;
                //if the accuracy is not better, remove all location updates for this listener
                if (location.getAccuracy() < MINIMUM_ACCURACY) {
                    fusedLocationProviderApi.removeLocationUpdates(_googleApiClient, this);
                }
            }
        }
    };

    private GoogleApiClient.ConnectionCallbacks _connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle bundle) {
            Location currentLocation = fusedLocationProviderApi.getLastLocation(_googleApiClient);
            if (currentLocation != null && currentLocation.getTime() > REFRESH_TIME) {
                _location = currentLocation;
            } else {
                fusedLocationProviderApi.requestLocationUpdates(_googleApiClient, _locationRequest, _locationListener);
                // Schedule a Thread to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(_googleApiClient, _locationListener);
                    }
                }, ONE_MIN, TimeUnit.MILLISECONDS);

                _location = fusedLocationProviderApi.getLastLocation(_googleApiClient);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            // TODO STUB .onConnectionSuspended()
            Log.v(TAG, "STUB .onConnectionSuspended()");
        }
    };

    private GoogleApiClient.OnConnectionFailedListener _connectionFailListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            // TODO STUB .onConnectionFailed()
            Log.v(TAG, "STUB .onConnectionFailed()");

        }
    };

}
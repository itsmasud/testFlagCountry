package com.fieldnation.ui;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class GPSLocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private final Context mContext;
    private static final String TAG = "GPSLocationService";

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private LocationClient locationClient;

    public GPSLocationService(Context context) {
        this.mContext = context;
        //turnGPSOn(this.mContext);
        startTracking();
    }

    @SuppressWarnings("deprecation")
    public static void turnGPSOn(Context context)
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (! provider.contains("gps"))
        { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    @SuppressWarnings("deprecation")
    public static void turnGPSOff(Context context)
    {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps"))
        { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    public void startTracking() {
        Log.d(TAG, "startTracking");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext) == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(this.mContext,this,this);

            if (!locationClient.isConnected() || !locationClient.isConnecting()) {
                locationClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
    }

    public boolean isGoogleServiceAvailable(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);

        boolean serviceConnected;
        if(ConnectionResult.SUCCESS == resultCode){
            serviceConnected = true;
        }else if(resultCode == ConnectionResult.SERVICE_MISSING){
            //TODO: Handle missing service. Display toast maybe.
            serviceConnected = false;
        }else{
            serviceConnected = false;
        }
        return serviceConnected;
    }

    /**
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        Log.e(TAG, "onDisconnected");

        stopSelf();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopSelf();
    }

    public double getLatitude(){
        double _latitude = 0.0;
        if(locationClient != null && locationClient.isConnected()){
            _latitude = locationClient.getLastLocation().getLatitude();
        }

        return _latitude;
    }

    public double getLongitude(){
        double _longitude = 0.0;
        if(locationClient != null && locationClient.isConnected()){
            _longitude = locationClient.getLastLocation().getLatitude();
        }

        return _longitude;
    }

    public Location getLatestLocation(){
        if(locationClient.isConnected() && isGoogleServiceAvailable()){
            return locationClient.getLastLocation();
        }else{
            return null;
        }
    }

    public LocationClient getLocationClient() {
        return locationClient;
    }
}
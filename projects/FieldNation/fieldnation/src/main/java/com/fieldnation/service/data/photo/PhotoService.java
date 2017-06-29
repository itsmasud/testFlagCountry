package com.fieldnation.service.data.photo;

import android.content.Intent;
import android.content.SharedPreferences;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.MultiThreadedService;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoService extends MultiThreadedService implements PhotoConstants {
    public static final String TAG = "PhotoService";

    private static final long DAY = 86400000;

    private long _imageDaysToLive = -1;
    private boolean _requireWifi = false;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences settings = App.get().getSharedPreferences();

        _imageDaysToLive = Integer.parseInt(settings.getString(getString(R.string.pref_key_remove_rate), "-1")) * 2;
        _requireWifi = settings.getBoolean(getString(R.string.pref_key_profile_photo_wifi_only), false);

        Log.v(TAG, "_imageDaysToLive: " + _imageDaysToLive + " _requireWifi: " + _requireWifi);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public void processIntent(Intent intent) {
        if (intent.hasExtra(PARAM_ACTION)) {
            String action = intent.getStringExtra(PARAM_ACTION);
            switch (action) {
                case PARAM_ACTION_GET:
                    get(intent);
                    break;
            }
        }
    }

    public void get(Intent intent) {
        Log.v(TAG, intent.getExtras().toString());
        String sourceUrl = intent.getStringExtra(PARAM_SOURCE_URL);
        boolean makeCircle = intent.getBooleanExtra(PARAM_IS_CIRCLE, false);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        String objectName = "PhotoCache" + (makeCircle ? "Circle" : "");

        // check cache
        StoredObject obj = StoredObject.get(this, App.getProfileId(), objectName, sourceUrl);

        if (obj != null) {
            PhotoDispatch.get(this, sourceUrl, obj.getUri(), makeCircle, true);

            if (!_requireWifi || App.get().haveWifi()) {
                if (_imageDaysToLive > -1) {
                    if (obj.getLastUpdated() + _imageDaysToLive * DAY < System.currentTimeMillis()) {
                        Log.v(TAG, "updating photo");
                        PhotoTransactionBuilder.get(this, objectName, sourceUrl, makeCircle, isSync);
                    }
                }
            }
        } else if (obj == null && (!_requireWifi || App.get().haveWifi())) {
            // doesn't exist, try to grab it.
            PhotoTransactionBuilder.get(this, objectName, sourceUrl, makeCircle, isSync);
        }
    }
}

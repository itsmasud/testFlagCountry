package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.SharedPreferences;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoService implements PhotoConstants {
    public static final String TAG = "PhotoService";

    private static final long DAY = 86400000;

    private static long _imageDaysToLive = -1;
    private static boolean _requireWifi = false;

    public static void get(Context context, String sourceUrl, boolean makeCircle, boolean isSync) {
        SharedPreferences settings = App.get().getSharedPreferences();
        _imageDaysToLive = Integer.parseInt(settings.getString(context.getString(R.string.pref_key_remove_rate), "-1")) * 2;
        _requireWifi = settings.getBoolean(context.getString(R.string.pref_key_profile_photo_wifi_only), false);

        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                String sourceUrl = (String) objects[1];
                boolean makeCircle = (Boolean) objects[2];
                boolean isSync = (Boolean) objects[3];
                String objectName = "PhotoCache" + (makeCircle ? "Circle" : "");

                // check cache
                StoredObject obj = StoredObject.get(context, App.getProfileId(), objectName, sourceUrl);

                if (obj != null) {
                    try {
                        PhotoDispatch.get(sourceUrl, obj.getUri(), makeCircle, true);

                        if (!_requireWifi || App.get().haveWifi()) {
                            if (_imageDaysToLive > -1) {
                                if (obj.getLastUpdated() + _imageDaysToLive * DAY < System.currentTimeMillis()) {
                                    Log.v(TAG, "updating photo");
                                    PhotoTransactionBuilder.get(context, objectName, sourceUrl, makeCircle, isSync);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                } else if (obj == null && (!_requireWifi || App.get().haveWifi())) {
                    // doesn't exist, try to grab it.
                    PhotoTransactionBuilder.get(context, objectName, sourceUrl, makeCircle, isSync);
                }
                return null;
            }
        }.executeEx(context, sourceUrl, makeCircle, isSync);
    }
}

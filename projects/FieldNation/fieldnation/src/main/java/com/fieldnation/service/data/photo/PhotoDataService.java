package com.fieldnation.service.data.photo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataService extends Service implements PhotoConstants {
    public static final String TAG = "PhotoDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Context context = (Context) params[0];
                    Intent intent = (Intent) params[1];
                    synchronized (LOCK) {
                        COUNT++;
                    }
                    processImage(context, intent);
                    synchronized (LOCK) {
                        COUNT--;
                        if (COUNT == 0) {
                            stopSelf();
                        }
                    }

                    return null;
                }
            }.executeEx(this, intent);
        }

        return START_STICKY;
    }

    public void processImage(Context context, Intent intent) {
        if (context == null)
            return;

        Log.v(TAG, intent.getExtras().toString());
        String url = intent.getStringExtra(PARAM_URL);
        boolean getCircle = intent.getBooleanExtra(PARAM_CIRCLE, false);
        String objectName = "PhotoCache" + (getCircle ? "Circle" : "");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        // check cache
        StoredObject obj = StoredObject.get(context, objectName, url);

        // if exists, then send result via broadcast
        // this code keeps images for ever
        if (obj != null) {
            PhotoDataDispatch.photo(context, obj.getFile(), url, getCircle);
        }

        if (obj == null) {
            // doesn't exist, try to grab it.
            PhotoTransactionBuilder.getPhoto(context, objectName, url, getCircle, isSync);
        }
    }
}

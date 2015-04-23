package com.fieldnation.service.data.profile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileDataService extends Service implements ProfileConstants {
    private static final String TAG = "ProfileDataService";


    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null) {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Context context = (Context) params[0];
                    Intent intent = (Intent) params[1];
                    synchronized (LOCK) {
                        COUNT++;
                    }
                    if (context != null) {
                        String action = intent.getStringExtra(PARAM_ACTION);
                        if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
                            getMyUserInformation(context, intent);
                        } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                            getAllNotifications(context, intent);
                        } else if (action.equals(PARAM_ACTION_GET_ALL_MESSAGES)) {
                            getAllMessages(context, intent);
                        }
                    }
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

    private static void getMyUserInformation(Context context, Intent intent) {
        Log.v(TAG, "getMyUserInformation");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        // get stored object
        StoredObject obj = StoredObject.get(context, PSO_PROFILE, PSO_MY_PROFILE_KEY);
        // if exists, then pass it back
        if (obj != null) {
            try {
                ProfileDispatch.myUserInformation(context, new JsonObject(obj.getData()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            // send request (we always ask for an update)
            ProfileTransactionBuilder.getProfile(context, false);
        }
    }

    private void getAllNotifications(Context context, Intent intent) {
        Log.v(TAG, "getAllNotifications");
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_NOTIFICATION_PAGE, page + "");
        if (obj != null) {
            try {
                ProfileDispatch.allNotifications(context, new JsonArray(obj.getData()), page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.getAllNotifications(context, page, isSync);
        }
    }

    private void getAllMessages(Context context, Intent intent) {
        Log.v(TAG, "getAllMessages");
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_MESSAGE_PAGE, page);
        if (obj != null) {
            try {
                ProfileDispatch.allMessages(context, new JsonArray(obj.getData()), page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.getAllMessages(context, page, isSync);
        }
    }

}

package com.fieldnation.service.data.profile;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileDataService extends MSService implements ProfileConstants {
    private static final String TAG = "ProfileDataService";

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents) {
        return new MyWorkerThread(manager, this, intents);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyWorkerThread extends WorkerThread {
        private String TAG = UniqueTag.makeTag("ProfileDataServiceThread");
        private Context _context;

        public MyWorkerThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            if (_context != null) {
                String action = intent.getStringExtra(PARAM_ACTION);
                if (action.equals(PARAM_ACTION_GET_MY_PROFILE)) {
                    getMyUserInformation(_context, intent);
                } else if (action.equals(PARAM_ACTION_GET_ALL_NOTIFICATIONS)) {
                    getAllNotifications(_context, intent);
                } else if (action.equals(PARAM_ACTION_GET_ALL_MESSAGES)) {
                    getAllMessages(_context, intent);
                }
            }
        }
    }

    private static void getMyUserInformation(Context context, Intent intent) {
        Log.v(TAG, "getMyUserInformation");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        // get stored object
        StoredObject obj = StoredObject.get(context, PSO_PROFILE, PSO_MY_PROFILE_KEY);
        // if exists, then pass it back
        if (obj != null) {
            try {
                ProfileDispatch.myUserInformation(context, new JsonObject(obj.getData()), isSync);
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
                ProfileDispatch.allNotifications(context, new JsonArray(obj.getData()), page, isSync);
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
                ProfileDispatch.allMessages(context, new JsonArray(obj.getData()), page, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.getAllMessages(context, page, isSync);
        }
    }

}

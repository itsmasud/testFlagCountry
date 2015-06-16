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
public class ProfileService extends MSService implements ProfileConstants {
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
                switch (action) {
                    case PARAM_ACTION_GET:
                        get(_context, intent);
                        break;
                    case PARAM_ACTION_LIST_NOTIFICATIONS:
                        listNotifications(_context, intent);
                        break;
                    case PARAM_ACTION_LIST_MESSAGES:
                        listMessages(_context, intent);
                        break;
                }
            }
        }
    }

    private static void get(Context context, Intent intent) {
        Log.v(TAG, "get");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        long profileId = intent.getLongExtra(PARAM_PROFILE_ID, 0);

        StoredObject obj = null;

        if (!isSync) {
            obj = StoredObject.get(context, PSO_PROFILE, profileId);
            // get stored object
            // if exists, then pass it back
            if (obj != null) {
                try {
                    ProfileDispatch.get(context, profileId, new JsonObject(obj.getData()), isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            // send request (we always ask for an update)
            ProfileTransactionBuilder.get(context, profileId, false);
        }
    }

    private void listNotifications(Context context, Intent intent) {
        Log.v(TAG, "listNotifications");
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = null;
        if (!isSync) {
            obj = StoredObject.get(context, PSO_NOTIFICATION_PAGE, page + "");
            if (obj != null) {
                try {
                    ProfileDispatch.listNotifications(context, new JsonArray(obj.getData()), page, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.listNotifications(context, page, isSync);
        }
    }

    private void listMessages(Context context, Intent intent) {
        Log.v(TAG, "listMessages");
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = null;

        if (!isSync) {
            obj = StoredObject.get(context, PSO_MESSAGE_PAGE, page);
            if (obj != null) {
                try {
                    ProfileDispatch.listMessages(context, new JsonArray(obj.getData()), page, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.listMessages(context, page, isSync);
        }
    }

}

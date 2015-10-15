package com.fieldnation.service.data.profile;

import android.content.Intent;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileService extends MSService implements ProfileConstants {
    private static final String TAG = "ProfileService";

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
                case PARAM_ACTION_LIST_NOTIFICATIONS:
                    listNotifications(intent);
                    break;
                case PARAM_ACTION_LIST_MESSAGES:
                    listMessages(intent);
                    break;
            }
        }
    }

    private void get(Intent intent) {
        Log.v(TAG, "get");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);
        long profileId = intent.getLongExtra(PARAM_PROFILE_ID, 0);

        StoredObject obj = null;

        if (!isSync && allowCache) {
            obj = StoredObject.get((int) profileId, PSO_PROFILE, profileId);
            // get stored object
            // if exists, then pass it back
            if (obj != null) {
                try {
                    ProfileDispatch.get(this, profileId, new JsonObject(obj.getData()), false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (isSync
                || !allowCache
                || obj == null
                || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            // send request (we always ask for an update)
            ProfileTransactionBuilder.get(this, profileId, false);
        }
    }

    private void listNotifications(Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);
        Log.v(TAG, "listNotifications(" + page + ", " + isSync + ")");

        StoredObject obj = null;
        if (!isSync && allowCache) {
            obj = StoredObject.get(App.getProfileId(), PSO_NOTIFICATION_PAGE, page + "");
            if (obj != null) {
                try {
                    ProfileDispatch.listNotifications(this, new JsonArray(obj.getData()), page, false, isSync, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (!allowCache
                || isSync
                || obj == null
                || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.listNotifications(this, page, isSync);
        }
    }

    private void listMessages(Intent intent) {
        Log.v(TAG, "listMessages");
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        StoredObject obj = null;

        if (!isSync && allowCache) {
            obj = StoredObject.get(App.getProfileId(), PSO_MESSAGE_PAGE, page);
            if (obj != null) {
                try {
                    ProfileDispatch.listMessages(this, new JsonArray(obj.getData()), page, false, isSync, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (!allowCache
                || isSync
                || obj == null
                || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            ProfileTransactionBuilder.listMessages(this, page, isSync);
        }
    }

}

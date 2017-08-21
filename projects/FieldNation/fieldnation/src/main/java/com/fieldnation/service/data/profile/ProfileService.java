package com.fieldnation.service.data.profile;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileService implements ProfileConstants {
    private static final String TAG = "ProfileService";

    public static void get(Context context, long profileId, boolean isSync, boolean allowCache) {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                long profileId = (Long) objects[1];
                boolean isSync = (Boolean) objects[2];
                boolean allowCache = (Boolean) objects[3];

                StoredObject obj = null;

                if (!isSync && allowCache) {
                    obj = StoredObject.get(context, (int) profileId, PSO_PROFILE, profileId);
                    // get stored object
                    // if exists, then pass it back
                    if (obj != null) {
                        try {
                            ProfileDispatch.get(profileId, new JsonObject(obj.getData()), false, isSync);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }
                    }
                }

                if (isSync
                        || !allowCache
                        || obj == null
                        || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
                    // send request (we always ask for an update)
                    ProfileTransactionBuilder.get(context, profileId, false);
                }
                return null;
            }
        }.executeEx(context, profileId, isSync, allowCache);
    }

    public static void listNotifications(Context context, int page, boolean isSync, boolean allowCache) {
        new AsyncTaskEx<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                int page = (Integer) objects[1];
                boolean isSync = (Boolean) objects[2];
                boolean allowCache = (Boolean) objects[3];

                StoredObject obj = null;
                if (!isSync && allowCache) {
                    obj = StoredObject.get(context, App.getProfileId(), PSO_NOTIFICATION_PAGE, page + "");
                    if (obj != null) {
                        try {
                            ProfileDispatch.listNotifications(new JsonArray(obj.getData()), page, false, isSync, true);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }
                    }
                }

                if (!allowCache
                        || isSync
                        || obj == null
                        || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
                    ProfileTransactionBuilder.listNotifications(context, page, isSync);
                }
                return null;
            }
        }.executeEx(context, page, isSync, allowCache);
    }

    public static void listMessages(Context context, int page, boolean isSync, boolean allowCache) {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                int page = (Integer) objects[1];
                boolean isSync = (Boolean) objects[2];
                boolean allowCache = (Boolean) objects[3];

                StoredObject obj = null;

                if (!isSync && allowCache) {
                    obj = StoredObject.get(context, App.getProfileId(), PSO_MESSAGE_PAGE, page);
                    if (obj != null) {
                        try {
                            ProfileDispatch.listMessages(new JsonArray(obj.getData()), page, false, isSync, true);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }
                    }
                }

                if (!allowCache
                        || isSync
                        || obj == null
                        || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
                    ProfileTransactionBuilder.listMessages(context, page, isSync);
                }
                return null;
            }
        }.executeEx(context, page, isSync, allowCache);
    }


    public static void uploadProfilePhoto(Context context, long profileId, String filePath, String filename) {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                long profileId = (Long) objects[1];
                String filePath = (String) objects[2];
                String filename = (String) objects[3];

                ProfileTransactionBuilder.uploadProfilePhoto(context, filename, filePath, profileId);
                return null;
            }
        }.executeEx(context, profileId, filePath, filename);
    }

    public static void uploadProfilePhoto(Context context, long profileId, String filename, Uri uri) {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                Context context = (Context) objects[0];
                long profileId = (Long) objects[1];
                String filename = (String) objects[2];
                Uri uri = (Uri) objects[3];

                if (uri != null) {
                    try {
                        StoredObject cache = StoredObject.get(context, App.getProfileId(), "CacheFile", uri.toString());
                        if (cache != null) {
                            ProfileTransactionBuilder.uploadProfilePhoto(context, cache, filename, uri.toString(), profileId);
                        } else {
                            ProfileTransactionBuilder.uploadProfilePhoto(context, context.getContentResolver().openInputStream(uri), filename, uri.toString(), profileId);
                        }
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
                return null;
            }
        }.executeEx(context, profileId, filename, uri);

    }
}
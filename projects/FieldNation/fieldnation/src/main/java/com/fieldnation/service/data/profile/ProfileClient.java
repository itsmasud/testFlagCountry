package com.fieldnation.service.data.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntools.AsyncTaskEx;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileClient extends Pigeon implements ProfileConstants {
    private static final String TAG = "ProfileDataClient";

    /*-*********************************-*/
    /*-             Commands            -*/
    /*-*********************************-*/
    public static void get(Context context) {
        get(context, 0, false, true);
    }

    public static void get(Context context, boolean allowCache) {
        get(context, 0, false, allowCache);
    }

    public static void get(Context context, long profileId, boolean isSync, boolean allowCache) {
        ProfileSystem.get(context, profileId, isSync, allowCache);
    }

    public void subGet() {
        subGet(false);
    }

    public void subGet(boolean isSync) {
        String topicId = ADDRESS_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        PigeonRoost.sub(this, topicId);
    }

    public void unsubGet() {
        unsubGet(false);
    }

    public void unsubGet(boolean isSync) {
        String topicId = ADDRESS_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        PigeonRoost.unsub(this, topicId);
    }

    public static void listNotifications(Context context, int page) {
        listNotifications(context, page, false, true);
    }

    public static void listNotifications(Context context, int page, boolean isSync, boolean allowCache) {
        ProfileSystem.listNotifications(context, page, isSync, allowCache);
    }

    public void subListNotifications() {
        subListNotifications(false);
    }

    public void subListNotifications(boolean isSync) {
        String topicId = ADDRESS_NOTIFICATION_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }
        PigeonRoost.sub(this, topicId);
    }

    public void unsubListNotifications() {
        unsubListNotifications(false);
    }

    public void unsubListNotifications(boolean isSync) {
        String topicId = ADDRESS_NOTIFICATION_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }
        PigeonRoost.unsub(this, topicId);
    }

    public static void listMessages(Context context, int page) {
        listMessages(context, page, false, true);
    }

    public static void listMessages(Context context, int page, boolean isSync, boolean allowCache) {
        ProfileSystem.listMessages(context, page, isSync, allowCache);
    }

    public void subListMessages() {
        subListMessages(false);
    }

    public void subListMessages(boolean isSync) {
        String topicId = ADDRESS_MESSAGE_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        PigeonRoost.sub(this, topicId);
    }

    public void unsubListMessages() {
        unsubListMessages(false);
    }

    public void unsubListMessages(boolean isSync) {
        String topicId = ADDRESS_MESSAGE_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        PigeonRoost.unsub(this, topicId);
    }

    public static void switchUser(Context context, long userId) {
        ProfileTransactionBuilder.switchUser(context, userId);
    }

    public void subSwitchUser() {
        PigeonRoost.sub(this, ADDRESS_SWITCH_USER);
    }

    public void unsubSwitchUser() {
        PigeonRoost.unsub(this, ADDRESS_SWITCH_USER);
    }

    public static void uploadProfilePhoto(Context context, long profileId, String filePath, String filename) {
        Log.v(TAG, "uploadProfilePhoto");

        ProfileDispatch.uploadProfilePhoto(filePath, false, false);
        ProfileSystem.uploadProfilePhoto(context, profileId, filePath, filename);
    }

    public static void uploadProfilePhoto(Context context, long profileId, String filename, Uri uri) {
        Log.v(TAG, "uploadProfilePhoto");

        ProfileDispatch.uploadProfilePhoto(filename, false, false);
        ProfileSystem.uploadProfilePhoto(context, profileId, filename, uri);
    }

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void actionAcceptTos(Context context, long userId) {
        ProfileTransactionBuilder.actionAcceptTos(context, userId);
    }

    public static void actionBlockCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        ProfileTransactionBuilder.actionBlockCompany(context, profileId, companyId, eventReasonId, explanation);
    }

    public static void actionRegisterDevice(Context context, String deviceId, long profileId) {
        ProfileTransactionBuilder.actionRegisterPhone(context, deviceId, profileId);
    }

    public void subActions() {
        subActions(0);
    }

    public void subActions(long profileId) {
        String topicId = ADDRESS_ACTION_COMPLETE;

        if (profileId > 0) {
            topicId += "/" + profileId;
        }

        PigeonRoost.sub(this, topicId);
    }

    public void unsubActions() {
        unsubActions(0);
    }

    public void unsubActions(long profileId) {
        String topicId = ADDRESS_ACTION_COMPLETE;

        if (profileId > 0) {
            topicId += "/" + profileId;
        }

        PigeonRoost.unsub(this, topicId);
    }

    /*-******************************-*/
    /*-          Listener            -*/
    /*-******************************-*/
    @Override
    public void onMessage(String address, Object message) {
        if (!(message instanceof Bundle)) {
            return;
        }
        Bundle bundle = (Bundle) message;
        if (address.startsWith(ADDRESS_GET)) {
            preGet(bundle);
        } else if (address.startsWith(ADDRESS_NOTIFICATION_LIST)) {
            preNotificationList(bundle);
        } else if (address.startsWith(ADDRESS_MESSAGE_LIST)) {
            preMessageList(bundle);
        } else if (address.startsWith(ADDRESS_ACTION_COMPLETE)) {
            preOnAction(bundle);
        } else if (address.startsWith(ADDRESS_SWITCH_USER)) {
            onSwitchUser(bundle.getLong(PARAM_USER_ID), bundle.getBoolean(PARAM_ERROR));
        } else if (address.startsWith(ADDRESS_UPLOAD_PHOTO)) {
            preUploadPhoto((Bundle) message);
        }
    }

    private void preUploadPhoto(Bundle payload) {
        if (payload.containsKey(PARAM_ERROR) && payload.getBoolean(PARAM_ERROR)) {
            preUploadPhoto(
                    payload.getString(PARAM_PHOTO_PATH),
                    payload.getBoolean(PARAM_IS_COMPLETE), true);
        } else {
            preUploadPhoto(
                    payload.getString(PARAM_PHOTO_PATH),
                    payload.getBoolean(PARAM_IS_COMPLETE), false);
        }
    }

    public void preUploadPhoto(String filename, boolean isComplete, boolean failed) {
    }

    public void onSwitchUser(long userId, boolean failed) {
    }

    private void preOnAction(Bundle payload) {
        onAction(payload.getLong(PARAM_PROFILE_ID),
                payload.getString(PARAM_ACTION),
                payload.getBoolean(PARAM_ERROR));
    }

    public void onAction(long profileId, String action, boolean failed) {
    }

    private void preGet(Bundle payload) {
        if (payload.getBoolean(PARAM_ERROR)) {
            onGet(null, true);
        } else {
            new AsyncTaskEx<Object, Object, Profile>() {
                @Override
                protected Profile doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];
                    try {
                        return Profile.fromJson((JsonObject) payload.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Profile o) {
                    if (o == null)
                        onGet(null, true);
                    else
                        onGet(o, false);
                }
            }.executeEx(payload);
        }
    }

    public void onGet(Profile profile, boolean failed) {
    }

    private void preNotificationList(Bundle payload) {
        if (payload.getBoolean(PARAM_ERROR)) {
            onNotificationList(null, payload.getInt(PARAM_PAGE), true, true);
        } else {
            new AsyncTaskEx<Bundle, Object, List<Notification>>() {
                private int page;
                private boolean isCached;

                @Override
                protected List<Notification> doInBackground(Bundle... params) {
                    Bundle payload = params[0];
                    try {
                        List<Notification> list = new LinkedList<>();
                        page = payload.getInt(PARAM_PAGE);
                        JsonArray jalerts = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        isCached = payload.getBoolean(PARAM_IS_CACHED);
                        for (int i = 0; i < jalerts.size(); i++) {
                            list.add(Notification.fromJson(jalerts.getJsonObject(i)));
                        }

                        return list;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Notification> notifications) {
                    if (notifications == null)
                        onNotificationList(null, page, true, isCached);
                    else
                        onNotificationList(notifications, page, false, isCached);
                }
            }.executeEx(payload);
        }
    }

    public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
    }

    private void preMessageList(Bundle payload) {
        if (payload.getBoolean(PARAM_ERROR)) {
            onMessageList(null, payload.getInt(PARAM_PAGE), true, true);
        } else {
            new AsyncTaskEx<Bundle, Object, List<Message>>() {
                private int page;
                private boolean isCached;

                @Override
                protected List<Message> doInBackground(Bundle... params) {
                    Bundle payload = params[0];
                    try {
                        List<Message> list = new LinkedList<>();
                        page = payload.getInt(PARAM_PAGE);
                        JsonArray jalerts = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        isCached = payload.getBoolean(PARAM_IS_CACHED);
                        for (int i = 0; i < jalerts.size(); i++) {
                            list.add(Message.fromJson(jalerts.getJsonObject(i)));
                        }

                        return list;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Message> messages) {
                    if (messages == null) {
                        onMessageList(null, page, true, isCached);
                    } else {
                        onMessageList(messages, page, false, isCached);
                    }
                }
            }.executeEx(payload);
        }
    }

    public void onMessageList(List<Message> list, int page, boolean failed, boolean isCached) {
    }
}

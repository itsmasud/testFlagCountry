package com.fieldnation.service.data.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnpigeon.TopicClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileClient extends TopicClient implements ProfileConstants {
    private static final String STAG = "ProfileDataClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public ProfileClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

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
        Intent intent = new Intent(context, ProfileService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_PROFILE_ID, profileId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subGet() {
        return subGet(false);
    }

    public boolean subGet(boolean isSync) {
        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        return register(topicId, TAG);
    }

    public static void listNotifications(Context context, int page) {
        listNotifications(context, page, false, true);
    }

    public static void listNotifications(Context context, int page, boolean isSync, boolean allowCache) {
        Intent intent = new Intent(context, ProfileService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subListNotifications() {
        return subListNotifications(false);
    }

    public boolean subListNotifications(boolean isSync) {
        String topicId = TOPIC_ID_NOTIFICATION_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }
        return register(topicId, TAG);
    }

    public static void listMessages(Context context, int page) {
        listMessages(context, page, false, true);
    }

    public static void listMessages(Context context, int page, boolean isSync, boolean allowCache) {
        Intent intent = new Intent(context, ProfileService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subListMessages() {
        return subListMessages(false);
    }

    public boolean subListMessages(boolean isSync) {
        String topicId = TOPIC_ID_MESSAGE_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        return register(topicId, TAG);
    }

    public static void switchUser(Context context, long userId) {
        ProfileTransactionBuilder.switchUser(context, userId);
    }

    public boolean subSwitchUser() {
        String topicId = TOPIC_ID_SWITCH_USER;
        return register(topicId, TAG);
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

    public boolean subActions() {
        return subActions(0);
    }

    public boolean subActions(long profileId) {
        String topicId = TOPIC_ID_ACTION_COMPLETE;

        if (profileId > 0) {
            topicId += "/" + profileId;
        }

        return register(topicId, TAG);
    }

    /*-******************************-*/
    /*-          Listener            -*/
    /*-******************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (!(payload instanceof Bundle)) {
                return;
            }
            Bundle bundle = (Bundle) payload;
            if (topicId.startsWith(TOPIC_ID_GET)) {
                preGet(bundle);
            } else if (topicId.startsWith(TOPIC_ID_NOTIFICATION_LIST)) {
                preNotificationList(bundle);
            } else if (topicId.startsWith(TOPIC_ID_MESSAGE_LIST)) {
                preMessageList(bundle);
            } else if (topicId.startsWith(TOPIC_ID_ACTION_COMPLETE)) {
                preOnAction(bundle);
            } else if (topicId.startsWith(TOPIC_ID_SWITCH_USER)) {
                onSwitchUser(bundle.getLong(PARAM_USER_ID), bundle.getBoolean(PARAM_ERROR));
            }
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
                            Log.v(STAG, ex);
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
                            Log.v(STAG, ex);
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
                            Log.v(STAG, ex);
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
}

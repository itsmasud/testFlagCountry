package com.fieldnation.service.data.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileDataClient extends TopicClient implements ProfileConstants {
    private String TAG = UniqueTag.makeTag("ProfileDataClient");

    public ProfileDataClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void acceptTos(Context context, long userId) {
        ProfileTransactionBuilder.acceptTos(context, userId);
    }

    public static void getProfile(Context context) {
        getProfile(context, false);
    }

    public static void getProfile(Context context, boolean isSync) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_MY_PROFILE);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean registerProfile() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_HAVE_PROFILE, TAG);
    }

    public static void getAllNotifications(Context context, int page) {
        getAllNotifications(context, page, false);
    }

    public static void getAllNotifications(Context context, int page, boolean isSync) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_ALL_NOTIFICATIONS);
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean registerAllNotifications() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_ALL_NOTIFICATION_LIST, TAG);
    }

    public static void getAllMessages(Context context, int page) {
        getAllMessages(context, page, false);
    }

    public static void getAllMessages(Context context, int page, boolean isSync) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_ALL_MESSAGES);
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean registerAllMessages() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_ALL_MESSAGES_LIST, TAG);
    }

    public void addBlockedCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        ProfileTransactionBuilder.postBlockedCompany(context, profileId, companyId, eventReasonId, explanation);
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
            if (topicId.equals(TOPIC_ID_HAVE_PROFILE)) {
                preProfile(bundle);
            } else if (topicId.equals(TOPIC_ID_ALL_NOTIFICATION_LIST)) {
                preAllNotificationPage(bundle);
            } else if (topicId.equals(TOPIC_ID_ALL_MESSAGES_LIST)) {
                preAllMessagesPage(bundle);
            }

        }

        protected void preProfile(Bundle payload) {
            new AsyncTaskEx<Object, Object, Profile>() {
                @Override
                protected Profile doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];
                    try {
                        return Profile.fromJson((JsonObject) payload.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Profile o) {
                    onProfile(o);
                }
            }.executeEx(payload);
        }

        public void onProfile(Profile profile) {
        }

        protected void preAllNotificationPage(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<Notification>>() {
                private int page;

                @Override
                protected List<Notification> doInBackground(Bundle... params) {
                    Bundle payload = params[0];
                    try {
                        List<Notification> list = new LinkedList<>();
                        page = payload.getInt(PARAM_PAGE);
                        JsonArray jalerts = (JsonArray) payload.getParcelable(PARAM_DATA_PARCELABLE);
                        for (int i = 0; i < jalerts.size(); i++) {
                            list.add(Notification.fromJson(jalerts.getJsonObject(i)));
                        }

                        return list;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Notification> notifications) {
                    onAllNotificationPage(notifications, page);
                }
            }.executeEx(payload);
        }

        public void onAllNotificationPage(List<Notification> list, int page) {
        }

        protected void preAllMessagesPage(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<Message>>() {
                private int page;

                @Override
                protected List<Message> doInBackground(Bundle... params) {
                    Bundle payload = params[0];
                    try {
                        List<Message> list = new LinkedList<>();
                        page = payload.getInt(PARAM_PAGE);
                        JsonArray jalerts = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        for (int i = 0; i < jalerts.size(); i++) {
                            list.add(Message.fromJson(jalerts.getJsonObject(i)));
                        }

                        return list;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Message> messages) {
                    onAllMessagesPage(messages, page);
                }
            }.executeEx(payload);
        }

        public void onAllMessagesPage(List<Message> list, int page) {
        }
    }
}

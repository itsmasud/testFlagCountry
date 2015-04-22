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
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.misc;

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

    @Override
    public void disconnect(Context context) {
        delete(TAG);
        super.disconnect(context);
    }

    public static void acceptTos(Context context, long userId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .useAuth(true)
                    .key("ProfileAcceptTos")
                    .request(
                            new HttpJsonBuilder()
                                    .method("POST")
                                    .path("/api/rest/v1/profile/" + userId + "/accept-tos")
                                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                    )
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getProfile(Context context) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_MY_PROFILE);
        context.startService(intent);
    }

    public boolean registerProfile() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_HAVE_PROFILE, TAG);
    }

    public static void getAllNotifications(Context context, int page) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_ALL_NOTIFICATIONS);
        intent.putExtra(PARAM_PAGE, page);
        context.startService(intent);
    }

    public boolean registerAllNotifications() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_ALL_NOTIFICATION_LIST, TAG);
    }

    public static void getAllMessages(Context context, int page) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_ALL_MESSAGES);
        intent.putExtra(PARAM_PAGE, page);
        context.startService(intent);
    }

    public boolean registerAllMessages() {
        if (!isConnected())
            return false;

        return register(TOPIC_ID_ALL_MESSAGES_LIST, TAG);
    }

    public void addBlockedCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .key("BlockCompany" + profileId + "/" + companyId)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .method("POST")
                                    .path("/api/rest/v1/profile/" + profileId + "/block/" + companyId)
                                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                                    .body("eventReasonId=" + eventReasonId
                                            + "&explanation=" + misc.escapeForURL(explanation))
                    )
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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

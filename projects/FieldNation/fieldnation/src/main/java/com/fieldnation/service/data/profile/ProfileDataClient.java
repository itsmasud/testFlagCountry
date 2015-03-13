package com.fieldnation.service.data.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class ProfileDataClient implements ProfileConstants {
    private String TAG = UniqueTag.makeTag("service.data.profile.ProfileDataClient");

    private TopicClient _client = null;
    private Listener _listener;

    public ProfileDataClient(Context context, Listener listener) {
        _listener = listener;
        _client = new TopicClient(_client_listener);
        _client.connect(context);
    }

    public void stop(Context context) {
        _client.disconnect(context);
    }

    public void acceptTos(Context context, long userId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .useAuth()
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

    public void getMyUserInformation(Context context) {
        Intent intent = new Intent(context, ProfileDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_MY_PROFILE);
        context.startService(intent);
    }

    public void getAllNotifications(Context context, int page) {
    }

    public void getAllMessages(Context context, int page) {

    }

    public void addBlockedCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {

    }

    private final TopicClient.Listener _client_listener = new TopicClient.Listener() {
        @Override
        public void onConnected() {
            _client.register(TOPIC_ID_HAVE_PROFILE, TAG);
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onRegistered(String topicId) {

        }

        @Override
        public void onEvent(String topicId, Bundle payload) {
            if (topicId.equals(TOPIC_ID_HAVE_PROFILE)) {
                try {
                    Profile profile = Profile.fromJson(new JsonObject(payload.getByteArray(PARAM_PROFILE)));
                    if (_listener != null)
                        _listener.onProfile(profile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    public interface Listener {
        public void onProfile(Profile profile);
    }


}

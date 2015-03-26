package com.fieldnation.service.auth;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.data.oauth.OAuth;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class AuthTopicClient extends TopicClient {
    private String TAG = UniqueTag.makeTag("AuthTopicClient");

    private static final String TOPIC_AUTH_STATE = "AuthTopicClient:TOPIC_AUTH_STATE";
    private static final String PARAM_STATE = "AuthTopicClient:PARAM_STATE";
    private static final String PARAM_OAUTH = "AuthTopicClient:PARAM_OAUTH";

    private static final String TOPIC_AUTH_COMMAND_REQUEST = "AuthTopicClient:TOPIC_AUTH_COMMAND_REQUEST";
    private static final String TOPIC_AUTH_COMMAND_INVALIDATE = "AuthTopicClient:TOPIC_AUTH_COMMAND_INVALIDATE";
    private static final String TOPIC_AUTH_COMMAND_REMOVE = "AuthTopicClient:TOPIC_AUTH_COMMAND_REMOVE";
    private static final String TOPIC_AUTH_COMMAND_ADDED_ACCOUNT = "AuthTopicClient:TOPIC_AUTH_COMMAND_ADDED_ACCOUNT";

    public AuthTopicClient(Listener listener) {
        super(listener);
    }

    // State

    public static void dispatchAuthState(Context context, AuthState state) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, state.ordinal());
        TopicService.dispatchEvent(context, TOPIC_AUTH_STATE, bundle, true);
    }

/*
    public static void dispatchNotAuthenticated(Context context) {
        dispatchAuthState(context, AuthState.NOT_AUTHENTICATED);
    }

    public static void dispatchAuthenticating(Context context) {
        dispatchAuthState(context, AuthState.AUTHENTICATING);
    }
*/

    public static void dispatchAuthenticated(Context context, OAuth auth) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, AuthState.AUTHENTICATED.ordinal());
        bundle.putParcelable(PARAM_OAUTH, auth);

        TopicService.dispatchEvent(context, TOPIC_AUTH_STATE, bundle, true);
    }

/*
    public static void dispatchRemoving(Context context) {
        dispatchAuthState(context, AuthState.REMOVING);
    }
*/

    public boolean registerAuthState() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_STATE, TAG);
    }

    public static void dispatchRequestCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REQUEST, null, false);
    }

    public boolean registerRequestCommand() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_COMMAND_REQUEST, TAG);
    }

    public static void dispatchInvalidateCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_INVALIDATE, null, false);
    }

    public boolean registerInvalidateCommand() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_COMMAND_INVALIDATE, TAG);
    }

    public static void dispatchRemoveCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REMOVE, null, false);
    }

    public boolean registerRemoveCommand() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_COMMAND_REMOVE, TAG);
    }

    public static void dispatchAddedAccountCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_ADDED_ACCOUNT, null, false);
    }

    public boolean registerAccountAddedCommand() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_COMMAND_ADDED_ACCOUNT, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (TOPIC_AUTH_STATE.equals(topicId)) {
                Bundle bundle = (Bundle) payload;
                AuthState state = AuthState.values()[bundle.getInt(PARAM_STATE)];

                switch (state) {
                    case AUTHENTICATED:
                        OAuth a = bundle.getParcelable(PARAM_OAUTH);
                        onAuthenticated(a);
                        break;
                    case AUTHENTICATING:
                        onAuthenticating();
                        break;
                    case NOT_AUTHENTICATED:
                        onNotAuthenticated();
                        break;
                    case REMOVING:
                        onRemoving();
                        break;
                }

            } else if (TOPIC_AUTH_COMMAND_INVALIDATE.equals(topicId)) {
                onCommandInvalidate();
            } else if (TOPIC_AUTH_COMMAND_REMOVE.equals(topicId)) {
                onCommandRemove();
            } else if (TOPIC_AUTH_COMMAND_REQUEST.equals(topicId)) {
                onCommandRequest();
            } else if (TOPIC_AUTH_COMMAND_ADDED_ACCOUNT.equals(topicId)) {
                onCommandAddedAccount();
            }
        }

        public void onCommandAddedAccount() {
        }

        public void onCommandInvalidate() {
        }

        public void onCommandRemove() {
        }

        public void onCommandRequest() {
        }

        public void onAuthenticating() {
        }

        public void onAuthenticated(OAuth oauth) {
        }

        public void onRemoving() {
        }

        public void onNotAuthenticated() {
        }

    }
}

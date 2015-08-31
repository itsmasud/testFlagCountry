package com.fieldnation.service.auth;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class AuthTopicClient extends TopicClient implements AuthTopicConstants {
    private static final String STAG = "AuthTopicClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public AuthTopicClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    // State
    public static void dispatchAuthState(Context context, AuthState state) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, state.ordinal());
        TopicService.dispatchEvent(context, TOPIC_AUTH_STATE, bundle, Sticky.FOREVER);
    }

    public static void dispatchAuthenticated(final Context context, OAuth auth) {
        if (context == null)
            return;

        new AsyncTaskEx<OAuth, Object, Bundle>() {
            @Override
            protected Bundle doInBackground(OAuth... params) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM_STATE, AuthState.AUTHENTICATED.ordinal());
                bundle.putParcelable(PARAM_OAUTH, params[0]);
                return bundle;
            }

            @Override
            protected void onPostExecute(Bundle bundle) {
                TopicService.dispatchEvent(context, TOPIC_AUTH_STATE, bundle, Sticky.FOREVER);
                super.onPostExecute(bundle);
            }
        }.executeEx(auth);
    }

    public boolean registerAuthState() {
        return register(TOPIC_AUTH_STATE, TAG);
    }

    public static void dispatchRequestCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REQUEST, null, Sticky.NONE);
    }

    public boolean registerRequestCommand() {
        return register(TOPIC_AUTH_COMMAND_REQUEST, TAG);
    }

    public static void dispatchInvalidateCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_INVALIDATE, null, Sticky.NONE);
    }

    public boolean registerInvalidateCommand() {
        return register(TOPIC_AUTH_COMMAND_INVALIDATE, TAG);
    }

    public static void dispatchRemoveCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REMOVE, null, Sticky.NONE);
    }

    public boolean registerRemoveCommand() {
        return register(TOPIC_AUTH_COMMAND_REMOVE, TAG);
    }

    public static void dispatchAddedAccountCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_ADDED_ACCOUNT, null, Sticky.NONE);
    }

    public boolean registerAccountAddedCommand() {
        return register(TOPIC_AUTH_COMMAND_ADDED_ACCOUNT, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_AUTH_STATE: {
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
                }
                case TOPIC_AUTH_COMMAND_INVALIDATE:
                    onCommandInvalidate();
                    break;
                case TOPIC_AUTH_COMMAND_REMOVE:
                    onCommandRemove();
                    break;
                case TOPIC_AUTH_COMMAND_REQUEST:
                    onCommandRequest();
                    break;
                case TOPIC_AUTH_COMMAND_ADDED_ACCOUNT:
                    onCommandAddedAccount();
                    break;
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

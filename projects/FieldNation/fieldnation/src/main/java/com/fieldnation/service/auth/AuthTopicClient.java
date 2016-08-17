package com.fieldnation.service.auth;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class AuthTopicClient extends TopicClient implements AuthTopicConstants {
    private static final String STAG = "AuthTopicClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public AuthTopicClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    // State
    public static void authStateChange(Context context, AuthState state) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, state.ordinal());
        TopicService.dispatchEvent(context, TOPIC_AUTH_STATE, bundle, Sticky.FOREVER);
    }

    public static void authenticated(final Context context, OAuth auth) {
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

    public boolean subAuthStateChange() {
        return register(TOPIC_AUTH_STATE);
    }

    public static void requestCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REQUEST, null, Sticky.NONE);
    }

    public boolean subRequestCommand() {
        return register(TOPIC_AUTH_COMMAND_REQUEST);
    }

    public static void invalidateCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_INVALIDATE, null, Sticky.NONE);
    }

    public boolean subInvalidateCommand() {
        return register(TOPIC_AUTH_COMMAND_INVALIDATE);
    }

    public static void removeCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_REMOVE, null, Sticky.NONE);
    }

    public boolean subRemoveCommand() {
        return register(TOPIC_AUTH_COMMAND_REMOVE);
    }

    public static void addedAccountCommand(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_ADDED_ACCOUNT, null, Sticky.NONE);
    }

    public boolean subAccountAddedCommand() {
        return register(TOPIC_AUTH_COMMAND_ADDED_ACCOUNT);
    }

    public static void needUsernameAndPassword(Context context, Parcelable authenticatorResponse) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_AUTHENTICATOR_RESPONSE, authenticatorResponse);

        TopicService.dispatchEvent(context, TOPIC_AUTH_COMMAND_NEED_PASSWORD, bundle, Sticky.FOREVER);
    }

    public boolean subNeedUsernameAndPassword() {
        return register(TOPIC_AUTH_COMMAND_NEED_PASSWORD);
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
                case TOPIC_AUTH_COMMAND_NEED_PASSWORD:
                    Bundle bundle = (Bundle) payload;
                    if (bundle.containsKey(PARAM_AUTHENTICATOR_RESPONSE)
                            && bundle.getParcelable(PARAM_AUTHENTICATOR_RESPONSE) != null) {
                        AuthTopicClient.needUsernameAndPassword(App.get(), null);
                        onNeedUsernameAndPassword(bundle.getParcelable(PARAM_AUTHENTICATOR_RESPONSE));
                    }
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

        public void onNeedUsernameAndPassword(Parcelable authenticatorResponse) {
        }
    }
}

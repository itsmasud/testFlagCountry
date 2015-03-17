package com.fieldnation.service.auth;

import android.content.Context;
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

    private static final String TOPIC_REQUEST_AUTH = "AuthTopicClient:TOPIC_REQUEST_AUTH";
    private static final String TOPIC_HAVE_AUTH = "AuthTopicClient:TOPIC_HAVE_AUTH";
    private static final String TOPIC_INVALID_AUTH = "AuthTopicClient:TOPIC_INVALID_AUTH";
    private static final String TOPIC_FAILED_AUTH = "AuthTopicClient:TOPIC_FAILED_AUTH";
    private static final String TOPIC_REMOVE_AUTH = "AuthTopicClient:TOPIC_REMOVE_AUTH";
    private static final String TOPIC_AUTH_REMOVED = "AuthTopicClient:TOPIC_AUTH_REMOVED";

    public AuthTopicClient(Listener listener) {
        super(listener);
    }

    public static void dispatchRequestAuth(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_REQUEST_AUTH, null, false);
    }

    public boolean registerRequestAuth() {
        if (!isConnected())
            return false;

        return register(TOPIC_REQUEST_AUTH, TAG);
    }

    public static void dispatchHaveAuth(Context context, OAuth oauth) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_HAVE_AUTH, oauth, true);
    }

    public boolean registerHaveAuth() {
        if (!isConnected())
            return false;

        return register(TOPIC_HAVE_AUTH, TAG);
    }

    public static void dispatchInvalidAuth(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_INVALID_AUTH, null, false);
    }

    public boolean registerInvalidAuth() {
        if (!isConnected())
            return false;

        return register(TOPIC_INVALID_AUTH, TAG);
    }

    public static void dispatchFailedAuth(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_FAILED_AUTH, null, false);
    }

    public boolean registerFailedAuth() {
        if (!isConnected())
            return false;

        return register(TOPIC_FAILED_AUTH, TAG);
    }

    public static void dispatchRemoveAuth(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_REMOVE_AUTH, null, false);
    }

    public boolean registerRemoveAuth() {
        if (!isConnected())
            return false;

        return register(TOPIC_REMOVE_AUTH, TAG);
    }

    public static void dispatchAuthRemoved(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_AUTH_REMOVED, null, false);
    }

    public boolean registerAuthRemoved() {
        if (!isConnected())
            return false;

        return register(TOPIC_AUTH_REMOVED, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (TOPIC_REQUEST_AUTH.equals(topicId)) {
                onAuthRequested();
            } else if (TOPIC_HAVE_AUTH.equals(topicId)) {
                onHaveAuth((OAuth) payload);
            } else if (TOPIC_FAILED_AUTH.equals(topicId)) {
                onAuthFailed();
            } else if (TOPIC_INVALID_AUTH.equals(topicId)) {
                onAuthInvalid();
            } else if (TOPIC_REMOVE_AUTH.equals(topicId)) {
                onRemoveAuth();
            } else if (TOPIC_AUTH_REMOVED.equals(topicId)) {
                onAuthRemoved();
            }
        }

        public void onAuthRequested() {
        }

        public void onHaveAuth(OAuth oauth) {
        }

        public void onAuthFailed() {
        }

        public void onAuthInvalid() {
        }

        public void onRemoveAuth() {
        }

        public void onAuthRemoved() {
        }
    }
}

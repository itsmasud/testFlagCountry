package com.fieldnation;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends TopicClient implements GlobalTopicConstants {
    private static final String STAG = "GlobalTopicClient";
    private String TAG = UniqueTag.makeTag("GlobalTopicClient");

    public GlobalTopicClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    // gcm
    public static void dispatchGcm(Context context, String message) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_GCM_MESSAGE, message);

        TopicService.dispatchEvent(context, TOPIC_GCM_MESSAGE, bundle, Sticky.NONE);
    }

    public boolean registerGcm() {
        return register(TOPIC_GCM_MESSAGE, TAG);
    }

    // update app
    public static void dispatchUpdateApp(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_APP_UPDATE, null, Sticky.FOREVER);
    }

    public boolean registerUpdateApp() {
        return register(TOPIC_APP_UPDATE, TAG);
    }

    // profile
    public static void dispatchGotProfile(Context context, Profile profile) {
        if (context == null)
            return;
        TopicService.dispatchEvent(context, TOPIC_GOT_PROFILE, profile, Sticky.FOREVER);
    }

    public boolean registerGotProfile() {
        return register(TOPIC_GOT_PROFILE, TAG);
    }

    // invalid
    public static void dispatchProfileInvalid(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_PROFILE_INVALID, null, Sticky.NONE);
    }

    public boolean registerProfileInvalid(Context context) {
        return register(TOPIC_PROFILE_INVALID, TAG);
    }

    // shutdown
    public static void dispatchAppShutdown(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_APP_UPDATE, null, Sticky.FOREVER);
    }

    public boolean registerAppShutdown() {
        return register(TOPIC_APP_UPDATE, TAG);
    }

    public boolean registerShowFeedbackDialog() {
        return register(TOPIC_ID_SHOW_FEEDBACK, TAG);
    }

    // feedback dialog
    public static void dispatchShowFeedbackDialog(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_FEEDBACK, null, Sticky.NONE);
    }

    public boolean registerShowHelpDialog() {
        return register(TOPIC_ID_SHOW_HELP_DIALOG, TAG);
    }

    // feedback dialog
    public static void dispatchShowHelpDialog(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_HELP_DIALOG, null, Sticky.NONE);
    }

    // NETWORK STATE
    public static void dispatchNetworkDisconnected(Context context) {
        Log.v(STAG, "dispatchNetworkDisconnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_DISCONNECTED);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void dispatchNetworkConnected(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTED);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void dispatchNetworkConnecting(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTING);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public boolean registerNetworkState() {
        return register(TOPIC_NETWORK_STATE, TAG);
    }

    // try connect
    public static void dispatchNetworkConnect(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }

    public boolean registerNetworkConnect() {
        return register(TOPIC_NETWORK_COMMAND_CONNECT, TAG);
    }


    // events
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_APP_UPDATE:
                    onNeedAppUpdate();
                    break;
                case TOPIC_GOT_PROFILE:
                    onGotProfile((Profile) payload);
                    break;
                case TOPIC_PROFILE_INVALID:
                    onProfileInvalid();
                    break;
                case TOPIC_SHUTDOWN:
                    onShutdown();
                    break;
                case TOPIC_ID_SHOW_FEEDBACK:
                    onShowFeedbackDialog();
                    break;
                case TOPIC_ID_SHOW_HELP_DIALOG:
                    onShowHelpDialog();
                    break;
                case TOPIC_NETWORK_STATE: {
                    switch (((Bundle) payload).getInt(PARAM_NETWORK_STATE)) {
                        case NETWORK_STATE_CONNECTED:
                            onNetworkConnected();
                            break;
                        case NETWORK_STATE_CONNECTING:
                            onNetworkConnecting();
                            break;
                        case NETWORK_STATE_DISCONNECTED:
                            onNetworkDisconnected();
                            break;
                    }
                    break;
                }
                case TOPIC_NETWORK_COMMAND_CONNECT:
                    onNetworkConnect();
                    break;
            }
        }

        public void onNeedAppUpdate() {
        }

        public void onGotProfile(Profile profile) {
        }

        public void onProfileInvalid() {
        }

        public void onShutdown() {
        }

        public void onShowFeedbackDialog() {
        }

        public void onShowHelpDialog() {
        }

        public void onNetworkDisconnected() {
        }

        public void onNetworkConnected() {
        }

        public void onNetworkConnect() {
        }

        public void onNetworkConnecting() {
        }
    }


}

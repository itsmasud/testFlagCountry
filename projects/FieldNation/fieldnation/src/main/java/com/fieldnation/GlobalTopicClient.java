package com.fieldnation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends TopicClient implements GlobalTopicConstants {
    private static final String STAG = "GlobalTopicClient";
    private final String TAG = UniqueTag.makeTag("GlobalTopicClient");

    public GlobalTopicClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    // gcm
    public static void gcm(Context context, String message) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_GCM_MESSAGE, message);

        TopicService.dispatchEvent(context, TOPIC_ID_GCM_MESSAGE, bundle, Sticky.NONE);
    }

    public boolean subGcm() {
        return register(TOPIC_ID_GCM_MESSAGE, TAG);
    }

    // update app
    public static void updateApp(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_APP_UPDATE, null, Sticky.FOREVER);
    }

    public boolean subUpdateApp() {
        return register(TOPIC_ID_APP_UPDATE, TAG);
    }

    // profile
    public static void gotProfile(Context context, Profile profile) {
        if (context == null)
            return;
        TopicService.dispatchEvent(context, TOPIC_ID_GOT_PROFILE, profile, Sticky.FOREVER);
    }

    public boolean subGotProfile() {
        return register(TOPIC_ID_GOT_PROFILE, TAG);
    }

    // invalid
    public static void profileInvalid(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_PROFILE_INVALID, null, Sticky.NONE);
    }

    public boolean subProfileInvalid(Context context) {
        return register(TOPIC_ID_PROFILE_INVALID, TAG);
    }

    // shutdown
    public static void appShutdown(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_SHUTDOWN, null, Sticky.NONE);
    }

    public boolean subAppShutdown() {
        return register(TOPIC_ID_SHUTDOWN, TAG);
    }

    // ContactUs dialog
    public static void showContactUsDialog(Context context, String source) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_CONTACT_US_SOURCE, source);

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_CONTACT_US, bundle, Sticky.NONE);
    }

    public boolean subShowContactUsDialog() {
        return register(TOPIC_ID_SHOW_CONTACT_US, TAG);
    }

    // NETWORK STATE
    public static void networkDisconnected(Context context) {
        Log.v(STAG, "networkDisconnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_DISCONNECTED);
        TopicService.dispatchEvent(context, TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnected(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTED);
        TopicService.dispatchEvent(context, TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnecting(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTING);
        TopicService.dispatchEvent(context, TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public boolean subNetworkState() {
        return register(TOPIC_ID_NETWORK_STATE, TAG);
    }

    // try connect
    public static void networkConnect(Context context) {
        Log.v(STAG, "networkConnect");
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_ID_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }

    public static Intent networkConnectIntent(Context context) {
        Log.v(STAG, "networkConnect");
        if (context == null)
            return null;

        return TopicService.dispatchEventIntent(context, TOPIC_ID_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }

    public boolean subNetworkConnect() {
        return register(TOPIC_ID_NETWORK_COMMAND_CONNECT, TAG);
    }


    // user switching

    public static void userSwitched(Context context, Profile profile) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_PROFILE, profile);

        TopicService.dispatchEvent(context, TOPIC_ID_USER_SWITCHED, bundle, Sticky.NONE);
    }

    public boolean subUserSwitched() {
        return register(TOPIC_ID_USER_SWITCHED, TAG);
    }

    public boolean subLoading() {
        return register(TOPIC_ID_SHOW_LOADING, TAG);
    }

    public static void setLoading(Context context, boolean isLoading) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_IS_LOADING, isLoading);

        TopicService.dispatchEvent(context, TOPIC_ID_SHOW_LOADING, bundle, Sticky.NONE);
    }

    // events
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_ID_APP_UPDATE:
                    onNeedAppUpdate();
                    break;
                case TOPIC_ID_GOT_PROFILE:
                    onGotProfile((Profile) payload);
                    break;
                case TOPIC_ID_PROFILE_INVALID:
                    onProfileInvalid();
                    break;
                case TOPIC_ID_SHUTDOWN:
                    onShutdown();
                    break;
                case TOPIC_ID_SHOW_CONTACT_US:
                    onShowContactUsDialog(((Bundle) payload).getString(PARAM_CONTACT_US_SOURCE));
                    break;
                case TOPIC_ID_SHOW_LOADING:
                    setLoading(((Bundle) payload).getBoolean(PARAM_IS_LOADING));
                    break;
                case TOPIC_ID_NETWORK_STATE: {
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
                case TOPIC_ID_NETWORK_COMMAND_CONNECT:
                    onNetworkConnect();
                    break;
                case TOPIC_ID_USER_SWITCHED:
                    onUserSwitched((Profile) ((Bundle) payload).getParcelable(PARAM_PROFILE));
                    break;
            }
        }

        public void onUserSwitched(Profile profile) {
        }

        public void onNeedAppUpdate() {
        }

        public void onGotProfile(Profile profile) {
        }

        public void onProfileInvalid() {
        }

        public void onShutdown() {
        }

        public void onShowContactUsDialog(String source) {
        }

        public void onNetworkDisconnected() {
        }

        public void onNetworkConnected() {
        }

        public void onNetworkConnect() {
        }

        public void onNetworkConnecting() {
        }

        public void setLoading(boolean isLoading) {
        }
    }
}

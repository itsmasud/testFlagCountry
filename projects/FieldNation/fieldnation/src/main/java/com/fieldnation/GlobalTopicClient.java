package com.fieldnation;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends TopicClient {
    private static final String STAG = "GlobalTopicClient";
    private String TAG = UniqueTag.makeTag("GlobalTopicClient");

    private static final String TOPIC_APP_UPDATE = "GlobalTopicClient:TOPIC_APP_UPDATE";
    private static final String TOPIC_GOT_PROFILE = "GlobalTopicClient:TOPIC_GOT_PROFILE";
    private static final String TOPIC_PROFILE_INVALID = "GlobalTopicClient:TOPIC_PROFILE_INVALID";
    private static final String TOPIC_SHUTDOWN = "GlobalTopicClient:TOPIC_SHUTDOWN";
    private static final String TOPIC_NETWORK_STATE = "GlobalTopicClient:TOPIC_NETWORK_STATE";
    private static final String PARAM_NETWORK_STATE = "NETWORK_STATE";
    private static final int NETWORK_STATE_CONNECTED = 1;
    private static final int NETWORK_STATE_DISCONNECTED = 2;
    private static final int NETWORK_STATE_CONNECTING = 3;

    private static final String TOPIC_NETWORK_COMMAND_CONNECT = "GlobalTopicClient:TOPIC_NETWORK_COMMAND_CONNECT";


    public GlobalTopicClient(Listener listener) {
        super(listener);
    }

    @Override
    public void disconnect(Context context) {
        delete(TAG);
        super.disconnect(context);
    }

    // update app
    public static void dispatchUpdateApp(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_APP_UPDATE, null, true);
    }

    public boolean registerUpdateApp() {
        if (!isConnected())
            return false;

        return register(TOPIC_APP_UPDATE, TAG);
    }

    // profile
    public static void dispatchGotProfile(Context context, Profile profile) {
        if (context == null)
            return;
        TopicService.dispatchEvent(context, TOPIC_GOT_PROFILE, profile, true);
    }

    public boolean registerGotProfile() {
        if (!isConnected())
            return false;

        return register(TOPIC_GOT_PROFILE, TAG);
    }

    // invalid
    public static void dispatchProfileInvalid(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_PROFILE_INVALID, null, false);
    }

    public boolean registerProfileInvalid(Context context) {
        if (!isConnected())
            return false;

        return register(TOPIC_PROFILE_INVALID, TAG);
    }

    // shutdown
    public static void dispatchAppShutdown(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_APP_UPDATE, null, false);
    }

    public boolean registerAppShutdown() {
        if (!isConnected())
            return false;

        return register(TOPIC_APP_UPDATE, TAG);
    }

    // NETWORK STATE
    public static void dispatchNetworkDisconnected(Context context) {
        Log.v(STAG, "dispatchNetworkDisconnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_DISCONNECTED);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, true);
    }

    public static void dispathNetworkConnected(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTED);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, true);
    }

    public static void dispatchNetworkConnecting(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTING);
        TopicService.dispatchEvent(context, TOPIC_NETWORK_STATE, bundle, true);
    }

    public boolean registerNetworkState() {
        if (!isConnected())
            return false;

        return register(TOPIC_NETWORK_STATE, TAG);
    }

    // try connect
    public static void dispatchNetworkConnect(Context context) {
        Log.v(STAG, "dispathNetworkConnected");
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_COMMAND_CONNECT, null, false);
    }

    public boolean registerNetworkConnect() {
        if (!isConnected())
            return false;
        return register(TOPIC_NETWORK_COMMAND_CONNECT, TAG);
    }


    // events
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (TOPIC_APP_UPDATE.equals(topicId)) {
                onNeedAppUpdate();
            } else if (TOPIC_GOT_PROFILE.equals(topicId) && payload instanceof Profile) {
                onGotProfile((Profile) payload);
            } else if (TOPIC_PROFILE_INVALID.equals(topicId)) {
                onProfileInvalid();
            } else if (TOPIC_SHUTDOWN.equals(topicId)) {
                onShutdown();
            } else if (TOPIC_NETWORK_STATE.equals(topicId)) {
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
            } else if (TOPIC_NETWORK_COMMAND_CONNECT.equals(topicId)) {
                onNetworkConnect();
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

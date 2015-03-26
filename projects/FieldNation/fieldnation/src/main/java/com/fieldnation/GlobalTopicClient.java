package com.fieldnation;

import android.content.Context;
import android.os.Parcelable;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends TopicClient {
    private String TAG = UniqueTag.makeTag("GlobalTopicClient");

    private static final String TOPIC_APP_UPDATE = "GlobalTopicClient:TOPIC_APP_UPDATE";
    private static final String TOPIC_GOT_PROFILE = "GlobalTopicClient:TOPIC_GOT_PROFILE";
    private static final String TOPIC_PROFILE_INVALID = "GlobalTopicClient:TOPIC_PROFILE_INVALID";
    private static final String TOPIC_SHUTDOWN = "GlobalTopicClient:TOPIC_SHUTDOWN";
    private static final String TOPIC_NETWORK_DISCONNECTED = "GlobalTopicClient:TOPIC_NETWORK_DISCONNECTED";
    private static final String TOPIC_NETWORK_CONNECTED = "GlobalTopicClient:TOPIC_NETWORK_CONNECTED";
    private static final String TOPIC_NETWORK_CONNECT = "GlobalTopicClient:TOPIC_NETWORK_CONNECT";
    private static final String TOPIC_NETWORK_CONNECTING = "GlobalTopicClient:TOPIC_NETWORK_CONNECTING";

    public GlobalTopicClient(Listener listener) {
        super(listener);
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

        misc.printStackTrace("GlobalTopicClient.dispatchGotProfile");

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

    // disconnected
    public static void dispatchNetworkDisconnected(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_DISCONNECTED, null, false);
    }

    public boolean registerNetworkDisconnected() {
        if (!isConnected())
            return false;

        return register(TOPIC_NETWORK_DISCONNECTED, TAG);
    }

    // connected
    public static void dispathNetworkConnected(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_CONNECTED, null, false);
    }

    public boolean registerNetworkConnected() {
        if (!isConnected())
            return false;

        return register(TOPIC_NETWORK_CONNECTED, TAG);
    }

    // try connect
    public static void dispatchNetworkConnect(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_CONNECT, null, false);
    }

    public boolean registerNetworkConnect() {
        if (!isConnected())
            return false;
        return register(TOPIC_NETWORK_CONNECT, TAG);
    }

    // connecting
    public static void dispatchNetworkConnecting(Context context) {
        if (context == null)
            return;

        TopicService.dispatchEvent(context, TOPIC_NETWORK_CONNECTING, null, false);
    }

    public boolean registerNetworkConnecting() {
        if (!isConnected())
            return false;
        return register(TOPIC_NETWORK_CONNECTING, TAG);
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
            } else if (TOPIC_NETWORK_DISCONNECTED.equals(topicId)) {
                onNetworkDisconnected();
            } else if (TOPIC_NETWORK_CONNECTED.equals(topicId)) {
                onNetworkConnected();
            } else if (TOPIC_NETWORK_CONNECT.equals(topicId)) {
                onNetworkConnect();
            } else if (TOPIC_NETWORK_CONNECTING.equals(topicId)) {
                onNetworkConnecting();
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

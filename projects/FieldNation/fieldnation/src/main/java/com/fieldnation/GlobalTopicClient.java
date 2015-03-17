package com.fieldnation;

import android.content.Context;
import android.os.Parcelable;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends TopicClient {
    private String TAG = UniqueTag.makeTag("GlobalTopicClient");

    private static final String TOPIC_APP_UPDATE = "GlobalTopicClient:TOPIC_APP_UPDATE";
    private static final String TOPIC_GOT_PROFILE = "GlobalTopicClient:TOPIC_GOT_PROFILE";
    private static final String TOPIC_PROFILE_INVALID = "GlobalTopicClient:TOPIC_PROFILE_INVALID";
    private static final String TOPIC_SHUTDOWN = "GlobalTopicClient:TOPIC_SHUTDOWN";

    public GlobalTopicClient(Listener listener) {
        super(listener);
    }

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

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (TOPIC_APP_UPDATE.equals(topicId)) {
                onNeedAppUpdate();
            } else if (TOPIC_GOT_PROFILE.equals(topicId)) {
                onGotProfile((Profile) payload);
            } else if (TOPIC_PROFILE_INVALID.equals(topicId)) {
                onProfileInvalid();
            }
        }

        public void onNeedAppUpdate() {
        }

        public void onGotProfile(Profile profile) {
        }

        public void onProfileInvalid() {
        }
    }


}

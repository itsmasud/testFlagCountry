package com.fieldnation;

import android.os.Bundle;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class GlobalTopicClient extends Pigeon implements GlobalTopicConstants {
    private static final String TAG = "GlobalTopicClient";

    public static void gcm(String message) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_GCM_MESSAGE, message);
        PigeonRoost.sendMessage(TOPIC_ID_GCM_MESSAGE, bundle, Sticky.NONE);
    }

    public void subGcm() {
        PigeonRoost.sub(this, TOPIC_ID_GCM_MESSAGE);
    }

    public void subUpdateApp() {
        PigeonRoost.sub(this, TOPIC_ID_APP_UPDATE);
    }

    public void subGotProfile() {
        PigeonRoost.sub(this, TOPIC_ID_GOT_PROFILE);
    }

    public void subProfileInvalid() {
        PigeonRoost.sub(this, TOPIC_ID_PROFILE_INVALID);
    }

    public void subAppShutdown() {
        PigeonRoost.sub(this, TOPIC_ID_SHUTDOWN);
    }

    public void subFinishActivity() {
        PigeonRoost.sub(this, TOPIC_ID_FINISH_ACTIVITY);
    }

    public void subNetworkState() {
        PigeonRoost.sub(this, TOPIC_ID_NETWORK_STATE);
    }

    public void subNetworkConnect() {
        PigeonRoost.sub(this, TOPIC_ID_NETWORK_COMMAND_CONNECT);
    }

    public void subUserSwitched() {
        PigeonRoost.sub(this, TOPIC_ID_USER_SWITCHED);
    }

    public void subLoading() {
        PigeonRoost.sub(this, TOPIC_ID_SHOW_LOADING);
    }

    public void unsubGcm() {
        PigeonRoost.unsub(this, TOPIC_ID_GCM_MESSAGE);
    }

    public void unsubUpdateApp() {
        PigeonRoost.unsub(this, TOPIC_ID_APP_UPDATE);
    }

    public void unsubGotProfile() {
        PigeonRoost.unsub(this, TOPIC_ID_GOT_PROFILE);
    }

    public void unsubProfileInvalid() {
        PigeonRoost.unsub(this, TOPIC_ID_PROFILE_INVALID);
    }

    public void unsubAppShutdown() {
        PigeonRoost.unsub(this, TOPIC_ID_SHUTDOWN);
    }

    public void unsubFinishActivity() {
        PigeonRoost.unsub(this, TOPIC_ID_FINISH_ACTIVITY);
    }

    public void unsubNetworkState() {
        PigeonRoost.unsub(this, TOPIC_ID_NETWORK_STATE);
    }

    public void unsubNetworkConnect() {
        PigeonRoost.unsub(this, TOPIC_ID_NETWORK_COMMAND_CONNECT);
    }

    public void unsubUserSwitched() {
        PigeonRoost.unsub(this, TOPIC_ID_USER_SWITCHED);
    }

    public void unsubLoading() {
        PigeonRoost.unsub(this, TOPIC_ID_SHOW_LOADING);
    }

    public static void updateApp() {
        PigeonRoost.sendMessage(TOPIC_ID_APP_UPDATE, null, Sticky.FOREVER);
    }

    public static void gotProfile(Profile profile) {
        PigeonRoost.sendMessage(TOPIC_ID_GOT_PROFILE, profile, Sticky.NONE);
    }

    public static void profileInvalid() {
        PigeonRoost.sendMessage(TOPIC_ID_PROFILE_INVALID, null, Sticky.NONE);
    }

    public static void appShutdown() {
        PigeonRoost.sendMessage(TOPIC_ID_SHUTDOWN, null, Sticky.NONE);
    }

    public static void finishActivity() {
        PigeonRoost.sendMessage(TOPIC_ID_FINISH_ACTIVITY, null, Sticky.NONE);
    }

    public static void networkDisconnected() {
        Log.v(TAG, "networkDisconnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_DISCONNECTED);
        PigeonRoost.sendMessage(TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnected() {
        Log.v(TAG, "dispathNetworkConnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTED);
        PigeonRoost.sendMessage(TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnecting() {
        Log.v(TAG, "dispathNetworkConnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTING);
        PigeonRoost.sendMessage(TOPIC_ID_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnect() {
        Log.v(TAG, "networkConnect");
        PigeonRoost.sendMessage(TOPIC_ID_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }

    public static void userSwitched(Profile profile) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_PROFILE, profile);
        PigeonRoost.sendMessage(TOPIC_ID_USER_SWITCHED, bundle, Sticky.NONE);
    }

    public static void setLoading(boolean isLoading) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_IS_LOADING, isLoading);
        PigeonRoost.sendMessage(TOPIC_ID_SHOW_LOADING, bundle, Sticky.NONE);
    }

/*
    public static Intent networkConnectIntent() {
        Log.v(TAG, "networkConnect");
        return TopicService.dispatchEventIntent(context, TOPIC_ID_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }
*/

    @Override
    public void onMessage(String address, Object message) {
        switch (address) {
            case TOPIC_ID_APP_UPDATE:
                onNeedAppUpdate();
                break;
            case TOPIC_ID_GOT_PROFILE:
                onGotProfile((Profile) message);
                break;
            case TOPIC_ID_PROFILE_INVALID:
                onProfileInvalid();
                break;
            case TOPIC_ID_SHUTDOWN:
                onShutdown();
                break;
            case TOPIC_ID_FINISH_ACTIVITY:
                onFinish();
                break;
            case TOPIC_ID_SHOW_LOADING:
                setLoading(((Bundle) message).getBoolean(PARAM_IS_LOADING));
                break;
            case TOPIC_ID_NETWORK_STATE: {
                switch (((Bundle) message).getInt(PARAM_NETWORK_STATE)) {
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
                onUserSwitched((Profile) ((Bundle) message).getParcelable(PARAM_PROFILE));
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

    public void onFinish() {
    }

    public void onNetworkDisconnected() {
    }

    public void onNetworkConnected() {
    }

    public void onNetworkConnect() {
    }

    public void onNetworkConnecting() {
    }

    public void onSetLoading(boolean isLoading) {
    }

/*
    public static abstract class ProfileSwitchListener extends Listener {

        public abstract GlobalTopicClient getGlobalTopicClient();

        @Override
        public void onConnected() {
            getGlobalTopicClient().subUserSwitched();
        }
    }
*/
}

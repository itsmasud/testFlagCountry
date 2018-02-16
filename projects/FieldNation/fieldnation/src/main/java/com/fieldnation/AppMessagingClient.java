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
public class AppMessagingClient extends Pigeon implements AppMessagingConstants {
    private static final String TAG = "AppMessagingClient";

    public static void gcm(String message) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_GCM_MESSAGE, message);
        PigeonRoost.sendMessage(ADDRESS_GCM_MESSAGE, bundle, Sticky.NONE);
    }

    public void subGcm() {
        PigeonRoost.sub(this, ADDRESS_GCM_MESSAGE);
    }

    public void subUpdateApp() {
        PigeonRoost.sub(this, ADDRESS_APP_UPDATE);
    }

    public void subGotProfile() {
        PigeonRoost.sub(this, ADDRESS_GOT_PROFILE);
    }

    public void subProfileInvalid() {
        PigeonRoost.sub(this, ADDRESS_PROFILE_INVALID);
    }

    public void subShutdownUI() {
        PigeonRoost.sub(this, ADDRESS_SHUTDOWN_UI);
    }

    public void subFinishActivity() {
        PigeonRoost.sub(this, ADDRESS_FINISH_ACTIVITY);
    }

    public void subNetworkState() {
        PigeonRoost.sub(this, ADDRESS_NETWORK_STATE);
    }

    public void subNetworkConnect() {
        PigeonRoost.sub(this, ADDRESS_NETWORK_COMMAND_CONNECT);
    }

    public void subUserSwitched() {
        PigeonRoost.sub(this, ADDRESS_USER_SWITCHED);
    }

    public void subLoading() {
        PigeonRoost.sub(this, ADDRESS_SHOW_LOADING);
    }

    public void subOfflineMode() {
        PigeonRoost.sub(this, ADDRESS_OFFLINE_MODE);
    }

    public void unsubGcm() {
        PigeonRoost.unsub(this, ADDRESS_GCM_MESSAGE);
    }

    public void unsubUpdateApp() {
        PigeonRoost.unsub(this, ADDRESS_APP_UPDATE);
    }

    public void unsubGotProfile() {
        PigeonRoost.unsub(this, ADDRESS_GOT_PROFILE);
    }

    public void unsubProfileInvalid() {
        PigeonRoost.unsub(this, ADDRESS_PROFILE_INVALID);
    }

    public void unsubShutdownUI() {
        PigeonRoost.unsub(this, ADDRESS_SHUTDOWN_UI);
    }

    public void unsubFinishActivity() {
        PigeonRoost.unsub(this, ADDRESS_FINISH_ACTIVITY);
    }

    public void unsubNetworkState() {
        PigeonRoost.unsub(this, ADDRESS_NETWORK_STATE);
    }

    public void unsubNetworkConnect() {
        PigeonRoost.unsub(this, ADDRESS_NETWORK_COMMAND_CONNECT);
    }

    public void unsubUserSwitched() {
        PigeonRoost.unsub(this, ADDRESS_USER_SWITCHED);
    }

    public void unsubLoading() {
        PigeonRoost.unsub(this, ADDRESS_SHOW_LOADING);
    }

    public void unsubOfflineMode() {
        PigeonRoost.unsub(this, ADDRESS_OFFLINE_MODE);
    }

    public static void updateApp() {
        PigeonRoost.sendMessage(ADDRESS_APP_UPDATE, null, Sticky.FOREVER);
    }

    public static void gotProfile(Profile profile) {
        PigeonRoost.sendMessage(ADDRESS_GOT_PROFILE, profile, Sticky.NONE);
    }

    public static void profileInvalid() {
        PigeonRoost.sendMessage(ADDRESS_PROFILE_INVALID, null, Sticky.NONE);
    }

    public static void appShutdown() {
        PigeonRoost.sendMessage(ADDRESS_SHUTDOWN_UI, null, Sticky.TEMP);
    }

    public static void finishActivity() {
        PigeonRoost.sendMessage(ADDRESS_FINISH_ACTIVITY, null, Sticky.NONE);
    }

    public static void networkDisconnected() {
        Log.v(TAG, "networkDisconnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_DISCONNECTED);
        PigeonRoost.sendMessage(ADDRESS_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnected() {
        Log.v(TAG, "dispathNetworkConnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTED);
        PigeonRoost.sendMessage(ADDRESS_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnecting() {
        Log.v(TAG, "dispathNetworkConnected");
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_NETWORK_STATE, NETWORK_STATE_CONNECTING);
        PigeonRoost.sendMessage(ADDRESS_NETWORK_STATE, bundle, Sticky.FOREVER);
    }

    public static void networkConnect() {
        Log.v(TAG, "networkConnect");
        PigeonRoost.sendMessage(ADDRESS_NETWORK_COMMAND_CONNECT, null, Sticky.NONE);
    }

    public static void userSwitched(Profile profile) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_PROFILE, profile);
        PigeonRoost.sendMessage(ADDRESS_USER_SWITCHED, bundle, Sticky.NONE);
    }

    public static void setLoading(boolean isLoading) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_IS_LOADING, isLoading);
        PigeonRoost.sendMessage(ADDRESS_SHOW_LOADING, bundle, Sticky.NONE);
    }

    public static void setOfflineMode(App.OfflineState isOffline) {
        App.get().setOffline(isOffline);
        Bundle bundle = new Bundle();
        bundle.putString("offline", isOffline.name());
        PigeonRoost.sendMessage(ADDRESS_OFFLINE_MODE, bundle, Sticky.FOREVER);
    }

    @Override
    public void onMessage(String address, Object message) {
        switch (address) {
            case ADDRESS_APP_UPDATE:
                onNeedAppUpdate();
                break;
            case ADDRESS_GOT_PROFILE:
                onGotProfile((Profile) message);
                break;
            case ADDRESS_PROFILE_INVALID:
                onProfileInvalid();
                break;
            case ADDRESS_SHUTDOWN_UI:
                onShutdownUI();
                break;
            case ADDRESS_FINISH_ACTIVITY:
                onFinish();
                break;
            case ADDRESS_SHOW_LOADING:
                onSetLoading(((Bundle) message).getBoolean(PARAM_IS_LOADING));
                break;
            case ADDRESS_NETWORK_STATE: {
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
            case ADDRESS_NETWORK_COMMAND_CONNECT:
                onNetworkConnect();
                break;
            case ADDRESS_USER_SWITCHED:
                onUserSwitched((Profile) ((Bundle) message).getParcelable(PARAM_PROFILE));
                break;
            case ADDRESS_OFFLINE_MODE:
                onOfflineMode(App.OfflineState.valueOf(((Bundle) message).getString("offline")));
                break;
        }
    }

    public void onOfflineMode(App.OfflineState state) {
    }

    public void onUserSwitched(Profile profile) {
    }

    public void onNeedAppUpdate() {
    }

    public void onGotProfile(Profile profile) {
    }

    public void onProfileInvalid() {
    }

    public void onShutdownUI() {
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
}

package com.fieldnation.service.auth;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public abstract class AuthTopicClient extends Pigeon implements AuthTopicConstants {
    private static final String TAG = "AuthTopicClient";

    // State
    public static void authStateChange(AuthState state) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, state.ordinal());
        PigeonRoost.sendMessage(ADDRESS_AUTH_STATE, bundle, Sticky.FOREVER);
    }

    public static void authenticated(OAuth auth) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_STATE, AuthState.AUTHENTICATED.ordinal());
        bundle.putParcelable(PARAM_OAUTH, auth);
        PigeonRoost.sendMessage(ADDRESS_AUTH_STATE, bundle, Sticky.FOREVER);
    }

    public void subAuthStateChange() {
        PigeonRoost.sub(this, ADDRESS_AUTH_STATE);
    }

    public void unsubAuthStateChange() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_STATE);
    }

    public static void requestCommand() {
        PigeonRoost.sendMessage(ADDRESS_AUTH_COMMAND_REQUEST, null, Sticky.NONE);
    }

    public void subRequestCommand() {
        PigeonRoost.sub(this, ADDRESS_AUTH_COMMAND_REQUEST);
    }

    public void unsubRequestCommand() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_COMMAND_REQUEST);
    }

    public static void invalidateCommand() {
        PigeonRoost.sendMessage(ADDRESS_AUTH_COMMAND_INVALIDATE, null, Sticky.NONE);
    }

    public void subInvalidateCommand() {
        PigeonRoost.sub(this, ADDRESS_AUTH_COMMAND_INVALIDATE);
    }

    public void unsubInvalidateCommand() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_COMMAND_INVALIDATE);
    }

    public static void removeCommand() {
        PigeonRoost.sendMessage(ADDRESS_AUTH_COMMAND_REMOVE, null, Sticky.NONE);
    }

    public void subRemoveCommand() {
        PigeonRoost.sub(this, ADDRESS_AUTH_COMMAND_REMOVE);
    }

    public void unsubRemoveCommand() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_COMMAND_REMOVE);
    }

    public static void addedAccountCommand() {
        PigeonRoost.sendMessage(ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT, null, Sticky.NONE);
    }

    public void subAccountAddedCommand() {
        PigeonRoost.sub(this, ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT);
    }

    public void unsubAccountAddedCommand() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT);
    }

    public static void needUsernameAndPassword(Parcelable authenticatorResponse) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_AUTHENTICATOR_RESPONSE, authenticatorResponse);
        PigeonRoost.sendMessage(ADDRESS_AUTH_COMMAND_NEED_PASSWORD, bundle, Sticky.FOREVER);
    }

    public void subNeedUsernameAndPassword() {
        PigeonRoost.sub(this, ADDRESS_AUTH_COMMAND_NEED_PASSWORD);
    }

    public void unsubNeedUsernameAndPassword() {
        PigeonRoost.unsub(this, ADDRESS_AUTH_COMMAND_NEED_PASSWORD);
    }

    @Override
    public void onMessage(String address, Parcelable message) {

        switch (address) {
            case ADDRESS_AUTH_STATE: {
                Bundle bundle = (Bundle) message;
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
            case ADDRESS_AUTH_COMMAND_INVALIDATE:
                onCommandInvalidate();
                break;
            case ADDRESS_AUTH_COMMAND_REMOVE:
                onCommandRemove();
                break;
            case ADDRESS_AUTH_COMMAND_REQUEST:
                onCommandRequest();
                break;
            case ADDRESS_AUTH_COMMAND_ADDED_ACCOUNT:
                onCommandAddedAccount();
                break;
            case ADDRESS_AUTH_COMMAND_NEED_PASSWORD:
                Bundle bundle = (Bundle) message;
                if (bundle.containsKey(PARAM_AUTHENTICATOR_RESPONSE)
                        && bundle.getParcelable(PARAM_AUTHENTICATOR_RESPONSE) != null) {
                    AuthTopicClient.needUsernameAndPassword(null);
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

package com.fieldnation.service.data.profile;

import android.os.Bundle;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class ProfileDispatch implements ProfileConstants {

    public static void get(long profileId, JsonObject data, boolean failed, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_PROFILE_ID, profileId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String address = ADDRESS_GET;

        if (isSync) {
            address += "_SYNC";
        }

        PigeonRoost.sendMessage(address, bundle, Sticky.FOREVER);
    }

    public static void listNotifications(JsonArray data, int page, boolean failed, boolean isSync, boolean isCached) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);
        bundle.putBoolean(PARAM_IS_CACHED, isCached);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String address = ADDRESS_NOTIFICATION_LIST;

        if (isSync) {
            address += "_SYNC";
        }

        PigeonRoost.sendMessage(address, bundle, Sticky.TEMP);
    }

    public static void listMessages(JsonArray data, int page, boolean failed, boolean isSync, boolean isCached) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        bundle.putInt(PARAM_PAGE, page);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);
        bundle.putBoolean(PARAM_ERROR, failed);
        bundle.putBoolean(PARAM_IS_CACHED, isCached);

        if (!failed)
            bundle.putParcelable(PARAM_DATA_PARCELABLE, data);

        String address = ADDRESS_MESSAGE_LIST;

        if (isSync) {
            address += "_SYNC";
        }

        PigeonRoost.sendMessage(address, bundle, Sticky.TEMP);
    }

    public static void switchUser(long userId, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_SWITCH_USER);
        bundle.putLong(PARAM_USER_ID, userId);
        bundle.putBoolean(PARAM_ERROR, failed);

        String address = ADDRESS_SWITCH_USER;

        PigeonRoost.sendMessage(address, bundle, Sticky.NONE);
    }

    public static void uploadProfilePhoto(String filePath, boolean isComplete, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_PHOTO_UPLOAD);
        bundle.putString(PARAM_PHOTO_PATH, filePath);
        bundle.putBoolean(PARAM_IS_COMPLETE, isComplete);
        bundle.putBoolean(PARAM_ERROR, failed);

        String address = ADDRESS_UPLOAD_PHOTO;

        PigeonRoost.sendMessage(address, bundle, Sticky.NONE);
    }


    public static void action(long profileId, String action, boolean failed) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, action);
        bundle.putLong(PARAM_PROFILE_ID, profileId);
        bundle.putBoolean(PARAM_ERROR, failed);

        String address = ADDRESS_ACTION_COMPLETE;

        if (profileId > 0) {
            address += "/" + profileId;
        }

        PigeonRoost.sendMessage(address, bundle, Sticky.TEMP);
    }
}
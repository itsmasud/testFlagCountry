package com.fieldnation.rpc.client;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class ProfileService extends WebService {
    private static final String TAG = "rpc.client.ProfileService";
    
    public static final String TOPIC_PROFILE_INVALIDATED = TAG + ":TOPIC_PROFILE_INVALIDATED";

    public ProfileService(Context context, String username, String authToken, ResultReceiver callback) {
        super(context, username, authToken, callback);
    }

    public Intent getMyUserInformation(int resultCode, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile", allowCache);
    }

    public Intent getUserInformation(int resultCode, long userId, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/" + userId, allowCache);
    }

    public Intent getAllNotifications(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/notifications/", "?page=" + page, allowCache);
    }

    public Intent getNewNotifications(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/notifications/new/", "?page=" + page, allowCache);
    }

    public Intent getUnreadMessages(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/messages/unread", "?page=" + page, allowCache);
    }

    public Intent getAllMessages(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/messages/", "?page=" + page, allowCache);
    }
}

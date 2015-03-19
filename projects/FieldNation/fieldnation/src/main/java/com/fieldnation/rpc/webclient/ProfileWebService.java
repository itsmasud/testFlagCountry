package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.service.data.oauth.OAuth;
import com.fieldnation.utils.misc;

public class ProfileWebService extends WebClientAuth {
    private static final String TAG = "rpc.client.ProfileService";

    public ProfileWebService(Context context, OAuth auth, ResultReceiver callback) {
        super(context, auth, callback);
    }

    public Intent acceptTos(int resultCode, long userId) {
        return httpPost(resultCode, "/api/rest/v1/profile/" + userId + "/accept-tos", null, "",
                "application/x-www-form-urlencoded", false);
    }

    public Intent getMyUserInformation(int resultCode, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile", allowCache);
    }

//    public Intent getUserInformation(int resultCode, long userId, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/profile/" + userId, allowCache);
//    }

    public Intent getAllNotifications(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/notifications/", "?page=" + page, allowCache);
    }

//    public Intent getNewNotifications(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/profile/notifications/new/", "?page=" + page, allowCache);
//    }
//
//    public Intent getUnreadMessages(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/profile/messages/unread", "?page=" + page, allowCache);
//    }

    public Intent getAllMessages(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/profile/messages/", "?page=" + page, allowCache);
    }

//    public Intent listBlockedCompanies(int resultCode, long profileId, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/profile/" + profileId + "/blocked", allowCache);
//    }
//
//    public Intent removeBlockedCompany(int resultCode, long profileId, long companyId) {
//        return httpDelete(resultCode, "/api/rest/v1/profile/" + profileId + "/blocked/" + companyId, null, false);
//    }

    public Intent addBlockedCompany(int resultCode, long profileId, long companyId, int eventReasonId, String explanation) {
        return httpPost(resultCode, "/api/rest/v1/profile/" + profileId + "/block/" + companyId, null,
                "eventReasonId=" + eventReasonId
                        + "&explanation=" + misc.escapeForURL(explanation),
                "application/x-www-form-urlencoded",
                false);
    }
}

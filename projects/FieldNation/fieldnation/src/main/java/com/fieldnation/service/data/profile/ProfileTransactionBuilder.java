package com.fieldnation.service.data.profile;

import android.content.Context;

import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class ProfileTransactionBuilder implements ProfileConstants {
    private static final String TAG = "ProfileTransactionBuilder";

    public static void get(Context context, long profileId, boolean isSync) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET");

            if (profileId > 0) {
                http.path("/api/rest/v1/profile/" + profileId);
            } else {
                http.path("/api/rest/v1/profile");
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pGet(profileId))
                    .key((isSync ? "Sync/" : "") + "ProfileGet")
                    .isSyncCall(isSync)
                    .useAuth(true)
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listNotifications(Context context, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/notifications")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pListNotifications(page))
                    .key((isSync ? "Sync/" : "") + "NotificationPage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/notifications")
                                    .urlParams("?page=" + page)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listMessages(Context context, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/messages")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pListMessages(page))
                    .key((isSync ? "Sync/" : "") + "MessagePage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/messages")
                                    .urlParams("?page=" + page)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void switchUser(Context context, long userId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/profile/[userId]/switch")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pSwitchUser(userId))
                    .key("SwitchUser / " + userId)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/" + userId + "/switch")
                    ).build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void action(Context context, long profileId, String action, String params,
                              String contentType, String body) {
        action(context, profileId, "POST/api/rest/v1/profile/[profileId]/" + action, action, params, contentType, body);
    }

    public static void action(Context context, long profileId, String timingKey, String action, String params,
                              String contentType, String body) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/profile/" + profileId + "/" + action);

            if (params != null) {
                http.urlParams(params);
            }

            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey(timingKey)
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pAction(profileId, action))
                    .useAuth(true)
                    .key("Profile/" + profileId + "/" + action)
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void actionAcceptTos(Context context, long profileId) {
        action(context, profileId, "accept-toc", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, null);
    }

    public static void actionBlockCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        action(context, profileId, "POST/api/rest/v1/profile/[profileId]/block/[CompanyId]", "block/" + companyId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "eventReasonId=" + eventReasonId
                        + "&explanation=" + misc.escapeForURL(explanation));
    }

    public static void actionRegisterPhone(Context context, String deviceId, long profileId) {
        try {
            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/api/record");

            JsonObject body = new JsonObject();
            body.put("ref", 1);
            body.put("record", "register-mobile-device");
            body.put("data.item_type", "gcm");
            body.put("data.device_id", deviceId);
            body.put("data.user_id", profileId);

            http.body("[" + body.toString() + "]");

            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, "application/json");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/api/record")
                    .priority(Priority.HIGH)
                    .listener(ProfileTransactionListener.class)
                    .listenerParams(ProfileTransactionListener.pAction(0, "register_device"))
                    .useAuth(true)
                    .key("Profile/RegisterDevice")
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}

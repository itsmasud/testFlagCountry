package com.fieldnation.service.data.profile;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class ProfileTransactionBuilder implements ProfileConstants {

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

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileTransactionHandler.class)
                    .handlerParams(ProfileTransactionHandler.pGet(profileId))
                    .key((isSync ? "Sync/" : "") + "ProfileGet")
                    .isSyncCall(isSync)
                    .useAuth(true)
                    .request(http)
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void listNotifications(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileTransactionHandler.class)
                    .handlerParams(ProfileTransactionHandler.pListNotifications(page))
                    .key((isSync ? "Sync/" : "") + "NotificationPage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/notifications")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void listMessages(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileTransactionHandler.class)
                    .handlerParams(ProfileTransactionHandler.pListMessages(page))
                    .key((isSync ? "Sync/" : "") + "MessagePage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile/messages")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void action(Context context, long profileId, String action, String params,
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

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileTransactionHandler.class)
                    .handlerParams(ProfileTransactionHandler.pAction(profileId, action))
                    .useAuth(true)
                    .key("Profile/" + profileId + "/" + action)
                    .request(http)
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void actionAcceptTos(Context context, long profileId) {
        action(context, profileId, "accept-tos", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, null);
    }

    public static void actionBlockCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        action(context, profileId, "block/" + companyId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "eventReasonId=" + eventReasonId
                        + "&explanation=" + misc.escapeForURL(explanation));
    }
}

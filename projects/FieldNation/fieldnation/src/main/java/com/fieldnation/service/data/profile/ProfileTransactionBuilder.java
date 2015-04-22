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

    public static void getProfile(Context context, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .handlerParams(ProfileWebTransactionHandler.generateGetProfileParams())
                    .key((isSync ? "Sync/" : "") + "ProfileGet")
                    .isSyncCall(isSync)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/profile")
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getAllNotifications(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(ProfileWebTransactionHandler.class)
                    .handlerParams(ProfileWebTransactionHandler.generateGetAllNotificationsParams(page))
                    .key((isSync ? "Sync/" : "") + "NotificationPage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/profile/notifications/")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getAllMessages(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(ProfileWebTransactionHandler.class)
                    .handlerParams(ProfileWebTransactionHandler.generateGetAllMessagesParams(page))
                    .key((isSync ? "Sync/" : "") + "MessagePage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/profile/messages/")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void acceptTos(Context context, long userId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .useAuth(true)
                    .key("ProfileAcceptTos")
                    .request(
                            new HttpJsonBuilder()
                                    .method("POST")
                                    .path("/api/rest/v1/profile/" + userId + "/accept-tos")
                                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                    )
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postBlockedCompany(Context context, long profileId, long companyId, int eventReasonId, String explanation) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(ProfileWebTransactionHandler.class)
                    .key("BlockCompany" + profileId + "/" + companyId)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .method("POST")
                                    .path("/api/rest/v1/profile/" + profileId + "/block/" + companyId)
                                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                                    .body("eventReasonId=" + eventReasonId
                                            + "&explanation=" + misc.escapeForURL(explanation))
                    )
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

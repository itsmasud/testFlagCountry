package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.service.transaction.WebTransactionSystem;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class WorkorderTransactionBuilder implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionBuilder";

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    private static void action(Context context, long workorderId, String action, String params, String contentType, String body) {
        action(context, workorderId, action, params, contentType, body, true);
    }

    private static void action(Context context, long workorderId, String action, String params, String contentType, String body, boolean useKey) {
        WebTransactionSystem.queueTransaction(context,
                action(workorderId, "POST", action, params, contentType, body,
                        "POST/api/rest/v1/workorder/[workorderId]/" + action,
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, action), useKey));
    }

    private static WebTransaction action(long workorderId, String method, String action, String params, String contentType, String body, Class<? extends WebTransactionListener> clazz, byte[] handlerParams) {
        return action(workorderId, method, action, params, contentType, body,
                method + "/api/rest/v1/workorder/[workorderId]/" + action, clazz, handlerParams, true);
    }

    private static WebTransaction action(long workorderId, String method, String action, String params, String contentType, String body, String timingKey, Class<? extends WebTransactionListener> clazz, byte[] handlerParams, boolean useKey) {
        App.get().setInteractedWorkorder();
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", action);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method(method)
                    .path("/api/rest/v1/workorder/" + workorderId + "/" + action);

            if (params != null) {
                http.urlParams(params);
            }

            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }

            WebTransaction.Builder builder = new WebTransaction.Builder()
                    .timingKey(timingKey)
                    .priority(Priority.HIGH)
                    .listener(clazz)
                    .listenerParams(handlerParams)
                    .useAuth(true)
                    .key("Workorder/" + workorderId + "/" + action)
                    .request(http)
                    .addTransform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()));

            if (useKey)
                builder.key("Workorder/" + workorderId + "/" + action);

            return builder.build();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        WebTransactionSystem.queueTransaction(context,
                actionRequestIntent(workorderId, expireInSeconds, null, null, null));
    }

    public static WebTransaction actionRequestIntent(long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
        String body = null;

        if (!misc.isEmptyOrNull(startTime)) {
            body = "start_time=" + startTime;
        }

        if (!misc.isEmptyOrNull(endTime)) {
            if (body == null)
                body = "end_time=" + endTime;
            else
                body += "&end_time=" + endTime;
        }

        if (!misc.isEmptyOrNull(note)) {
            if (body == null)
                body = "note=" + misc.escapeForURL(note);
            else
                body += "&note=" + misc.escapeForURL(note);
        }

        if (expireInSeconds != -1) {
            if (body == null)
                body = "expiration=" + expireInSeconds;
            else
                body += "&expiration=" + expireInSeconds;
        }

        return action(workorderId, "POST", "request", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pActionRequest(workorderId, expireInSeconds, startTime, endTime, note));
    }

    public static WebTransaction actionAcceptIntent(long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        String body = null;

        if (!misc.isEmptyOrNull(startTimeIso8601))
            body = "start_time=" + startTimeIso8601;

        if (!misc.isEmptyOrNull(endTimeIso8601)) {
            if (body == null)
                body = "end_time=" + endTimeIso8601;
            else
                body += "&end_time=" + endTimeIso8601;
        }

        if (!misc.isEmptyOrNull(note)) {
            if (body == null)
                body = "note=" + misc.escapeForURL(note);
            else
                body += "&note=" + misc.escapeForURL(note);
        }

        return action(workorderId, "POST", "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pAccept(workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta));
    }

    public static void actionDecline(Context context, long workorderId) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "");
    }

    public static WebTransaction actionReadyIntent(long workorderId) {
        return action(workorderId, "POST", "ready", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "",
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pAction(workorderId, "ready")
        );
    }

    public static void actionWithdrawRequest(Context context, long workorderId) {
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", "withdraw-request");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/withdraw-request")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "delete_request"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/withdraw-request"))
                    .addTransform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()))
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static WebTransaction actionPostRatingIntent(long workorderId, int satisfactionRating, int scopeRating, int respectRating, int respectComment, boolean recommendBuyer, String otherComments) {
        try {
            String body = "";

            // parameterized body
            body += "topic=android";
            body += "&satisfaction_rating=" + satisfactionRating;
            body += "&scope_rating=" + scopeRating;
            body += "&respect_rating=" + respectRating;
            body += respectComment == -1 ? "" : "&respect_comment=" + respectComment;
            body += recommendBuyer == false ? "" : "&recommend_buyer=" + recommendBuyer;
            body += misc.isEmptyOrNull(otherComments) ? "" : "&other_comments=" + otherComments;

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/rate");

            http.body(body);
            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/rate")
                    .priority(Priority.LOW)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pRating(
                            satisfactionRating, scopeRating, respectRating, respectComment,
                            recommendBuyer, otherComments, workorderId))
                    .useAuth(true)
                    .request(http)
                    .build();

            return transaction;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }
}


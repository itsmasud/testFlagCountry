package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.v2.data.model.ExpenseCategory;

import java.io.InputStream;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class WorkorderTransactionBuilder implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionBuilder";

    public static void get(Context context, long workorderId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/details")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pDetails(workorderId))
                    .key((isSync ? "Sync/" : "") + "Workorder/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/details"))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void list(Context context, WorkorderDataSelector selector, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/" + selector.getCall())
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pList(page, selector))
                    .key((isSync ? "Sync/" : "") + "WorkorderList/" + selector.ordinal() + "_" + selector.getCall() + "/" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + selector.getCall())
                            .urlParams("?page=" + page))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listAlerts(Context context, long workorderId, boolean isRead, boolean isSync) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v1/workorder/" + workorderId + "/notifications");

            if (isRead) {
                builder.urlParams("?mark_read=1");
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/notifications")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAlertList(workorderId, isRead))
                    .key((isSync ? "Sync/" : "") + "WorkorderAlertList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listTasks(Context context, long workorderId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/tasks")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pTaskList(workorderId))
                    .key((isSync ? "Sync/" : "") + "WorkorderTaskList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/tasks"))
                    .build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void getBundle(Context context, long bundleId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/bundle/[bundleId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pGetBundle(bundleId))
                    .key((isSync ? "Sync/" : "") + "GetBundle/" + bundleId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body) {
        action(context, workorderId, action, params, contentType, body, true);
    }

    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body, boolean useKey) {
        context.startService(
                action(context, workorderId, "POST", action, params, contentType, body,
                        "POST/api/rest/v1/workorder/[workorderId]/" + action,
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, action), useKey));
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, Class<? extends WebTransactionListener> clazz,
                                 byte[] handlerParams) {
        return action(context, workorderId, method, action, params, contentType, body,
                method + "/api/rest/v1/workorder/[workorderId]/" + action, clazz, handlerParams, true);
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, String timingKey, Class<? extends WebTransactionListener> clazz,
                                 byte[] handlerParams, boolean useKey) {
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

            return WebTransactionService.makeQueueTransactionIntent(context, builder.build());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static void sendAcknowledge(Context context, long workorderId, String action) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, action))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/" + action))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns work order details
    public static void actionChangePay(Context context, long workorderId, Pay pay, String explanation) {
        String payload = "";
        if (pay != null) {
            if (pay.isPerDeviceRate()) {
                payload += "payBasis=per_device";
                payload += "&payPerDevice=" + pay.getPerDevice();
                payload += "&maxDevices=" + pay.getMaxDevice();
            } else if (pay.isBlendedRate()) {
                payload += "payBasis=blended";
                payload += "&hourlyRate=" + pay.getBlendedStartRate();
                payload += "&maxHours=" + pay.getBlendedFirstHours();
                payload += "&additionalHourRate=" + pay.getBlendedAdditionalRate();
                payload += "&additionalMaxHours=" + pay.getBlendedAdditionalHours();
            } else if (pay.isFixedRate()) {
                payload += "payBasis=fixed";
                payload += "&fixedTotalAmount=" + pay.getFixedAmount();
            } else if (pay.isHourlyRate()) {
                payload += "payBasis=per_hour";
                payload += "&hourlyRate=" + pay.getPerHour();
                payload += "&maxHours=" + pay.getMaxHour();
            }
        }

        payload += "&providerExplanation=" + misc.escapeForURL(explanation);

        action(context, workorderId, "pay-change", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, payload);
    }

    // returns the new message
    public static void actionAddMessage(Context context, long workorderId, String message) {
        action(context, workorderId, "messages/new", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "message=" + misc.escapeForURL(message), false);
    }

    // returns the custom field value
    public static void actionCustomField(Context context, long workorderId, long customFieldId, String value) {
        context.startService(
                action(context, workorderId, "POST", "custom-fields/" + customFieldId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        (misc.isEmptyOrNull(value) ? "" : "value=" + misc.escapeForURL(value)),
                        "POST/api/rest/v1/workorder/[workorderId]/custom-fields/[customFieldId]",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, "custom-fields"), true));
    }

    // returns the modified task, not the work order details or task list
    public static void actionCompleteTask(Context context, long workorderId, long taskId) {
        context.startService(
                action(context, workorderId, "POST", "tasks/complete/" + taskId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "",
                        "POST/api/rest/v1/workorder/[workorderId]/tasks/complete/[taskId]",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, "tasks/complete"), true));
    }

    // returns the entire work order details
    public static void actionComplete(Context context, long workorderId) {
        context.startService(
                action(context, workorderId, "POST", "complete", null, null, null,
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pComplete(workorderId)));
    }

    // returns the entire work order details
    public static void actionIncomplete(Context context, long workorderId) {
        action(context, workorderId, "incomplete", null, null, null);
    }

    // returns an error/success message
    public static void actionReportProblem(Context context, long workorderId, String explanation, ReportProblemType type, Integer delayInSeconds) {
        if (misc.isEmptyOrNull(explanation)) {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "type=" + type.value
                            + (delayInSeconds == null ? "" : "&delay=" + delayInSeconds));
        } else {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "explanation=" + misc.escapeForURL(explanation)
                            + "&type=" + type.value
                            + (delayInSeconds == null ? "" : "&delay=" + delayInSeconds));
        }
    }

    // returns the entire work order details
    public static void actionCheckin(Context context, long workorderId, String dateTime) {
        context.startService(action(
                context, workorderId, "POST", "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + dateTime,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckIn(workorderId)));
    }


    public static void actionCheckin(Context context, long workorderId, Location location, String dateTime) {
        context.startService(action(
                context, workorderId, "POST", "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + dateTime
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckIn(workorderId)));
    }


    public static void actionCheckout(Context context, long workorderId, String dateTime) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + dateTime,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, String dateTime, Location location) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + dateTime
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, String dateTime, int deviceCount) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + dateTime,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, String dateTime, int deviceCount, Location location) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + dateTime
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCheckOut(workorderId)));
    }


    // returns the full work order details
    public static void actionClosingNotes(Context context, long workorderId, String closingNotes) {
        action(context, workorderId, "closing-notes", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "notes=" + (closingNotes == null ? "" : misc.escapeForURL(closingNotes)));
    }

    // returns work order details
    public static void actionAcknowledgeHold(Context context, long workorderId) {
        context.startService(
                action(context, workorderId, "GET", "acknowledge-hold", null, null, null,
                        "GET/api/rest/v1/workorder/[workorderId]/acknowledge-hold",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, "acknowledge-hold"), true));
    }

    // returns error/success state
    public static void actionCounterOffer(
            Context context, long workorderId, boolean expires, String reason,
            int expiresAfterInSecond, Pay pay, Schedule schedule, Expense[] expenses) {

        context.startService(
                actionCounterOfferIntent(context, workorderId, expires, reason, expiresAfterInSecond,
                        pay, schedule, expenses)
        );
    }

    // returns error message
    public static Intent actionCounterOfferIntent(
            Context context, long workorderId, boolean expires, String reason,
            int expiresAfterInSecond, Pay pay, Schedule schedule, Expense[] expenses) {

        String payload = "";
        // reason/expire
        if (expires)
            payload += "expires=true&expiresAfterInSecond=" + expiresAfterInSecond;
        else
            payload += "expires=false";

        if (!misc.isEmptyOrNull(reason)) {
            payload += "&providerExplanation=" + misc.escapeForURL(reason);
        }

        // pay counter
        if (pay != null) {
            if (pay.isPerDeviceRate()) {
                payload += "&payBasis=per_device";
                payload += "&payPerDevice=" + pay.getPerDevice();
                payload += "&maxDevices=" + pay.getMaxDevice();
            } else if (pay.isBlendedRate()) {
                payload += "&payBasis=blended";
                payload += "&hourlyRate=" + pay.getBlendedStartRate();
                payload += "&maxHours=" + pay.getBlendedFirstHours();
                payload += "&additionalHourRate=" + pay.getBlendedAdditionalRate();
                payload += "&additionalMaxHours=" + pay.getBlendedAdditionalHours();
            } else if (pay.isFixedRate()) {
                payload += "&payBasis=fixed";
                payload += "&fixedTotalAmount=" + pay.getFixedAmount();
            } else if (pay.isHourlyRate()) {
                payload += "&payBasis=per_hour";
                payload += "&hourlyRate=" + pay.getPerHour();
                payload += "&maxHours=" + pay.getMaxHour();
            }
        }
        // schedule counter
        if (schedule != null) {
            if (schedule.isExact()) {
                payload += "&startTime=" + schedule.getStartTime();
            } else {
                payload += "&startTime=" + schedule.getStartTime();
                payload += "&endTime=" + schedule.getEndTime();
            }
        }
        // expenses counter
        if (expenses != null && expenses.length > 0) {
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < expenses.length; i++) {
                Expense expense = expenses[i];
                json.append("{\"description\":\"").append(expense.getDescription()).append("\",");
                json.append("\"price\":\"").append(expense.getPrice()).append("\"}");
                if (i < expenses.length - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            payload += "&expenses=" + json.toString();
        }

        return action(context, workorderId, "POST", "counter_offer", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, payload,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pCounterOffer(
                        workorderId, expires, reason, expiresAfterInSecond, pay, schedule, expenses));
    }

    // returns entire work order
    public static void actionRequest(Context context, long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
        context.startService(
                actionRequestIntent(context, workorderId, expireInSeconds, startTime, endTime, note));
    }

    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        context.startService(
                actionRequestIntent(context, workorderId, expireInSeconds, null, null, null));
    }

    public static Intent actionRequestIntent(Context context, long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
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

        return action(context, workorderId, "POST", "request", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pActionRequest(workorderId, expireInSeconds, startTime, endTime, note));
    }


    // returns work order details
    public static void actionAccept(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        Intent intent = actionAcceptIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
        context.startService(intent);
    }

    public static Intent actionAcceptIntent(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
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

        return action(context, workorderId, "POST", "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pAccept(workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta));
    }

    public static void actionConfirm(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note) {
        Intent intent = actionConfirmIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note);
        context.startService(intent);
    }

    public static Intent actionConfirmIntent(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note) {
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

        return action(context, workorderId, "POST", "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pConfirm(workorderId, startTimeIso8601, endTimeIso8601, note));
    }

    // returns the details
    public static void actionDecline(Context context, long workorderId) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "");
    }

    public static void actionReady(Context context, long workorderId) {
        context.startService(actionReadyIntent(context, workorderId));
    }

    // return the details
    public static Intent actionReadyIntent(Context context, long workorderId) {
        return action(context, workorderId, "POST", "ready", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "",
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pAction(workorderId, "ready")
        );
    }

    // returns work order details
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

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


    public static Intent actionPostRatingIntent(Context context, long workorderId, int satisfactionRating, int scopeRating,
                                                int respectRating, int respectComment, boolean recommendBuyer, String otherComments) {
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

            return WebTransactionService.makeQueueTransactionIntent(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    /*-************************************-*/
    /*-             Signatures             -*/
    /*-************************************-*/
    public static void getSignature(Context context, long workorderId, long signatureId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/signature/[signatureId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pGetSignature(workorderId, signatureId))
                    .key((isSync ? "Sync/" : "") + "GetSignature/" + workorderId + "/" + signatureId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns signature list
    public static void deleteSignature(Context context, long workorderId, long signatureId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/signature/[signatureId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "delete_signature"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


/*
    public static void addSignatureJson(Context context, long workorderId, String name, String json) {
        action(context, workorderId, "signature", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "signatureFormat=json"
                        + "&printName=" + misc.escapeForURL(name)
                        + "&signature=" + json);
    }
*/

    // returns the signature list
    public static void addSignatureSvg(Context context, long workorderId, String name, String svg) {
        action(context, workorderId, "signature", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "signatureFormat=svg"
                        + "&printName=" + misc.escapeForURL(name)
                        + "&signature=" + svg);
    }

/*
    public static void addSignatureJsonTask(Context context, long workorderId, long taskId, String name, String json) {
        action(context, workorderId, "tasks/complete/" + taskId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "print_name=" + misc.escapeForURL(name)
                        + "&signature_json=" + json);
    }
*/

    // returns task list
    // TODO make sure this works
    public static void addSignatureSvgTask(Context context, long workorderId, long taskId, String name, String svg) {
        context.startService(
                action(context, workorderId, "POST", "tasks/complete/" + taskId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "print_name=" + misc.escapeForURL(name) + "&signature_svg=" + svg,
                        "POST/api/rest/v1/workorder/[workorderId]/tasks/complete/[taskId]",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pAction(workorderId, "tasks/complete"), true));
    }

    /*-**************************************-*/
    /*-             Deliverables             -*/
    /*-**************************************-*/
    public static void getDeliverable(Context context, long workorderId, long deliverableId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/deliverables/[deliverableId]")
                    .priority(Priority.HIGH)
                    .listener(DeliverableTransactionListener.class)
                    .listenerParams(DeliverableTransactionListener.pGet(workorderId, deliverableId))
                    .key((isSync ? "Sync/" : "") + "GetDeliverable/" + workorderId + "/" + deliverableId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the deliverable details
/*
    public static void uploadDeliverable(Context context, String filePath, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable file");
        StoredObject upFile = StoredObject.put(context, App.getProfileId(), "TempFile", filePath, new File(filePath), "uploadTemp.dat");
        uploadDeliverable(context, upFile, filename, photoDescription, workorderId, uploadSlotId);
    }
*/

    public static void uploadDeliverable(Context context, InputStream inputStream, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable uri");
        StoredObject upFile = StoredObject.put(context, App.getProfileId(), "TempFile", filename, inputStream, "uploadTemp.dat");
        uploadDeliverable(context, upFile, filename, photoDescription, workorderId, uploadSlotId);
    }

    public static void uploadDeliverable(Context context, StoredObject upFile, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable uri");

        if (upFile == null) {
            ToastClient.toast(context, "Unknown error uploading file, please try again", Toast.LENGTH_SHORT);
            Log.logException(new Exception("PA-332 - UpFile is null"));
            WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, false, true);
            return;
        }


        if (upFile.isUri() && upFile.getUri() != null) {
            if (upFile.size() > 100000000) { // 100 MB?
                StoredObject.delete(context, upFile);
                ToastClient.toast(context, "File is too long: " + filename, Toast.LENGTH_LONG);
                WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, false, true);
                return;
            }
        }

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .multipartField("note", (photoDescription == null ? "" : misc.escapeForURL(photoDescription)))
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/deliverables")
                    .priority(Priority.LOW)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pUploadDeliverable(workorderId, uploadSlotId, filename))
                    .useAuth(true)
                    .request(builder)
                    .setWifiRequired(App.get().onlyUploadWithWifi())
                    .setTrack(true)
                    .setTrackType(TrackerEnum.DELIVERABLES)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns deliverable list
    public static void deleteDeliverable(Context context, long workorderId, long workorderUploadId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/deliverables/[workorderUploadId]")
                    .priority(Priority.HIGH)
                    .listener(DeliverableTransactionListener.class)
                    .listenerParams(DeliverableTransactionListener.pChange(workorderId))
                    .useAuth(true)
                    .key("Workorder/DeleteDeliverable/" + workorderId + "/" + workorderUploadId)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("DELETE")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + workorderUploadId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-**********************************-*/
    /*-             Messages             -*/
    /*-**********************************-*/
    public static void listMessages(Context context, long workorderId, boolean isRead, boolean isSync) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v1/workorder/" + workorderId + "/messages");

            if (isRead) {
                builder.urlParams("?mark_read=1");
            }

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/messages")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pMessageList(workorderId, isRead))
                    .key((isSync ? "Sync/" : "") + "WorkorderMessageList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-**********************************-*/
    /*-             Discount             -*/
    /*-**********************************-*/
    // returns a discount list
    public static void createDiscount(Context context, long workorderId, String description, double price) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/discount")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "create_discount"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/discount")
                            .body("description=" + misc.escapeForURL(description) + "&amount=" + misc.escapeForURL(price + "")))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns a discount list
    public static void deleteDiscount(Context context, long workorderId, long discountId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/discounts/[discountId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "delete_discount"))
                    .key("Workorder/DeleteDiscount/" + workorderId + "/" + discountId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/discounts/" + discountId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Expense             -*/
    /*-*********************************-*/
    // returns boolean
    public static void createExpense(Context context, long workorderId, String description, double price,
                                     ExpenseCategory category) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/expense")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "create_expense"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense")
                            .body(("description=" + misc.escapeForURL(description)
                                    + "&price=" + misc.escapeForURL(price + "")
                                    + "&category_id=" + category.getId()).trim()))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the expense
    public static void deleteExpense(Context context, long workorderId, long expenseId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/expense/[expenseId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "delete_expense"))
                    .key("Workorder/DeleteExpense/" + workorderId + "/" + expenseId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-***********************************-*/
    /*-             Time Logs             -*/
    /*-***********************************-*/
    // returns details
    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate) {
        context.startService(
                action(context, workorderId, "POST", "log", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate),
                        "POST/api/rest/v1/workorder/[workorderId]/log",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate, int numberOfDevices) {
        context.startService(
                action(context, workorderId, "POST", "log", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate)
                                + "&noOfDevices=" + numberOfDevices,
                        "POST/api/rest/v1/workorder/[workorderId]/log",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate) {
        context.startService(
                action(context, workorderId, "POST", "log/" + loggedHoursId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate),
                        "POST/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        context.startService(
                action(context, workorderId, "POST", "log/" + loggedHoursId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate)
                                + "&noOfDevices=" + numberOfDevices,
                        "POST/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]",
                        WorkorderTransactionListener.class,
                        WorkorderTransactionListener.pTimeLog(workorderId), true));
    }

    // returns details
    public static void deleteTimeLog(Context context, long workorderId, long loggedHoursId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "DELETE_LOG"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-***********************************-*/
    /*-             Shipments             -*/
    /*-***********************************-*/
    // returns the shipment data
    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber) {
        context.startService(postShipmentIntent(context, workorderId, description, isToSite,
                carrier, carrierName, trackingNumber));
    }

    // returns the shipment data
    public static Intent postShipmentIntent(Context context, long workorderId, String description, boolean isToSite,
                                            String carrier, String carrierName, String trackingNumber) {
        return action(context, workorderId, "POST", "shipments", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=" + carrier
                        + "&carrier_name=" + (carrierName == null ? "" : misc.escapeForURL(carrierName))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber),
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pActionCreateShipment(workorderId, description,
                        isToSite, carrier, carrierName, trackingNumber, -1));
    }

    // returns the shipment data
    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber, long taskId) {
        context.startService(postShipmentIntent(context, workorderId, description, isToSite,
                carrier, carrierName, trackingNumber, taskId));
    }

    // returns the shipment data
    public static Intent postShipmentIntent(Context context, long workorderId, String description, boolean isToSite,
                                            String carrier, String carrierName, String trackingNumber, long taskId) {
        return action(context, workorderId, "POST", "shipments", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=" + carrier
                        + "&carrier_name=" + (carrierName == null ? "" : misc.escapeForURL(carrierName))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber)
                        + "&task_id=" + taskId,
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pActionCreateShipment(workorderId,
                        description, isToSite, carrier, carrierName, trackingNumber, taskId));
    }

    // returns the task list
    public static void actionCompleteShipmentTask(Context context, long workorderId, long shipmentId, long taskId) {
        context.startService(actionCompleteShipmentTaskIntent(context, workorderId, shipmentId, taskId));
    }

    // returns the task list
    public static Intent actionCompleteShipmentTaskIntent(Context context, long workorderId, long shipmentId, long taskId) {
        return action(context, workorderId, "POST", "tasks/complete/" + taskId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "shipment_id=" + shipmentId,
                "POST/api/rest/v1/workorder/[workorderId]/tasks/complete/[taskId]",
                WorkorderTransactionListener.class,
                WorkorderTransactionListener.pActionCompleteShipmentTask(workorderId, shipmentId, taskId), true);
    }

    //    public static void postShipment(Context context, long workorderId, long shipmentId, String description, boolean isToSite,
//                                    String carrier, String carrierName, String trackingNumber) {
//        try {
//            WebTransactionBuilder.builder(context)
//                    .priority(Priority.HIGH)
//                    .listener(WorkorderTransactionListener.class)
//                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "create_shipment"))
//                    .useAuth(true)
//                    .request(new HttpJsonBuilder()
//                            .protocol("https")
//                            .method("POST")
//                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
//                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId)
//                            .body("description=" + misc.escapeForURL(description)
//                                    + "&direction=" + (isToSite ? "to_site" : "from_site")
//                                    + "&carrier=" + carrier
//                                    + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
//                                    + "&tracking_number=" + misc.escapeForURL(trackingNumber)))
//                    .send();
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
//    }

    // returns shipment lists
    public static void deleteShipment(Context context, long workorderId, long shipmentId) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/shipments/[shipmentId]")
                    .priority(Priority.HIGH)
                    .listener(WorkorderTransactionListener.class)
                    .listenerParams(WorkorderTransactionListener.pAction(workorderId, "delete_shipment"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

/*
    public static void actionCompleteSignatureTask(Context context, long workorderId, long taskId, String printName, String signatureJson) {
        action(context, workorderId, "tasks/complete/" + taskId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "print_name=" + misc.escapeForURL(printName)
                        + "&signature_json=" + signatureJson);
    }
*/
}


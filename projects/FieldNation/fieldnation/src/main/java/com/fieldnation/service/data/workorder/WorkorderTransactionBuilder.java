package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class WorkorderTransactionBuilder implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionBuilder";

    public static void get(Context context, long workorderId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .key((isSync ? "Sync/" : "") + "Workorder/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/[workorderId]/details")
                            .path("/api/rest/v1/workorder/" + workorderId + "/details"))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void list(Context context, WorkorderDataSelector selector, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pList(page, selector))
                    .key((isSync ? "Sync/" : "") + "WorkorderList/" + selector.ordinal() + "_" + selector.getCall() + "/" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/" + selector.getCall())
                            .path("/api/rest/v1/workorder/" + selector.getCall())
                            .urlParams("?page=" + page))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listAlerts(Context context, long workorderId, boolean isRead, boolean isSync) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/notifications")
                    .path("/api/rest/v1/workorder/" + workorderId + "/notifications");

            if (isRead) {
                builder.urlParams("?mark_read=1");
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAlertList(workorderId, isRead))
                    .key((isSync ? "Sync/" : "") + "WorkorderAlertList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(builder)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listTasks(Context context, long workorderId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pTaskList(workorderId))
                    .key((isSync ? "Sync/" : "") + "WorkorderTaskList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/[workorderId]/tasks")
                            .path("/api/rest/v1/workorder/" + workorderId + "/tasks"))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void getBundle(Context context, long bundleId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pGetBundle(bundleId))
                    .key((isSync ? "Sync/" : "") + "GetBundle/" + bundleId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/bundle/[bundleId]")
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .send();
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
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, action), useKey));
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, Class<? extends WebTransactionHandler> clazz,
                                 byte[] handlerParams) {
        return action(context, workorderId, method, action, params, contentType, body,
                method + "/api/rest/v1/workorder/[workorderId]/" + action, clazz, handlerParams, true);
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, String timingKey, Class<? extends WebTransactionHandler> clazz,
                                 byte[] handlerParams, boolean useKey) {
        App.get().setInteractedWorkorder();
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", action);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method(method)
                    .timingKey(timingKey)
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

            WebTransactionBuilder builder = WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(clazz)
                    .handlerParams(handlerParams)
                    .useAuth(true)
                    .key("Workorder/" + workorderId + "/" + action)
                    .request(http)
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()));

            if (useKey)
                builder.key("Workorder/" + workorderId + "/" + action);

            return builder.makeIntent();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static void sendAcknowledge(Context context, long workorderId, String action) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, action))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/" + action))
                    .send();
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
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, "custom-fields"), true));
    }

    // returns the modified task, not the work order details or task list
    public static void actionCompleteTask(Context context, long workorderId, long taskId) {
        context.startService(
                action(context, workorderId, "POST", "tasks/complete/" + taskId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "",
                        "POST/api/rest/v1/workorder/[workorderId]/tasks/complete/[taskId]",
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, "tasks/complete"), true));
    }

    // returns the entire work order details
    public static void actionComplete(Context context, long workorderId) {
        action(context, workorderId, "complete", null, null, null);
    }

    // returns the entire work order details
    public static void actionIncomplete(Context context, long workorderId) {
        action(context, workorderId, "incomplete", null, null, null);
    }

    // returns an error/success message
    public static void actionReportProblem(Context context, long workorderId, String explanation, ReportProblemType type) {
        if (misc.isEmptyOrNull(explanation)) {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "type=" + type.value);
        } else {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "explanation=" + misc.escapeForURL(explanation) + "&type=" + type.value);
        }
    }

    // returns the entire work order details
    public static void actionCheckin(Context context, long workorderId) {
        context.startService(action(
                context, workorderId, "POST", "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + ISO8601.now(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckIn(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckin(Context context, long workorderId, Location location) {
        context.startService(action(
                context, workorderId, "POST", "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckIn(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + ISO8601.now(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, Location location) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, int deviceCount) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + ISO8601.now(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckOut(workorderId)));
    }

    // returns the entire work order details
    public static void actionCheckout(Context context, long workorderId, int deviceCount, Location location) {
        context.startService(action(
                context, workorderId, "POST", "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude(),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCheckOut(workorderId)));
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
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, "acknowledge-hold"), true));
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
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCounterOffer(
                        workorderId, expires, reason, expiresAfterInSecond, pay, schedule, expenses));
    }

    // returns entire work order
    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        context.startService(
                actionRequestIntent(context, workorderId, expireInSeconds));
    }

    public static Intent actionRequestIntent(Context context, long workorderId, long expireInSeconds) {
        String body = null;

        if (expireInSeconds != -1) {
            body = "expiration=" + expireInSeconds;
        }

        return action(context, workorderId, "POST", "request", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionRequest(workorderId, expireInSeconds));
    }


    // returns work order details
    public static void actionConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        Intent intent = actionConfirmAssignmentIntent(context, workorderId, startTimeIso8601, endTimeIso8601);
        context.startService(intent);
    }

    public static Intent actionConfirmAssignmentIntent(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        return action(context, workorderId, "POST", "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "start_time=" + startTimeIso8601 + "&end_time=" + endTimeIso8601,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pAssignment(workorderId, startTimeIso8601, endTimeIso8601));
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
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pAction(workorderId, "ready")
        );
    }

    // returns work order details
    public static void actionWithdrawRequest(Context context, long workorderId) {
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", "withdraw-request");

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "delete_request"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/withdraw-request")
                            .path("/api/rest/v1/workorder/" + workorderId + "/withdraw-request"))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


    public static Intent actionPostRatingIntent(Context context, int satisfactionRating, int scopeRating,
                                                int respectRating, int respectComment, boolean recommendBuyer, String otherComments, long workorderId) {
        try {
            String body = "";

            // parameterized body
            body += "topic=android";
            body += "&satisfaction_rating=" + satisfactionRating;
            body += "&scope_rating=" + scopeRating;
            body += "&respect_rating=" + respectRating;
            body += "&respect_comment=" + respectComment;
            body += "&recommend_buyer=" + recommendBuyer;
            body += "&other_comments=" + otherComments;

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/rate")
                    .path("/api/rest/v1/workorder/" + workorderId + "/rate");

            http.body(body);
            http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);

            return WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pRating(
                            satisfactionRating, scopeRating, respectRating, respectComment,
                            recommendBuyer, otherComments, workorderId))
                    .useAuth(true)
                    .request(http)
                    .makeIntent();
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
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pGetSignature(workorderId, signatureId))
                    .key((isSync ? "Sync/" : "") + "GetSignature/" + workorderId + "/" + signatureId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/[workorderId]/signature/[signatureId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns signature list
    public static void deleteSignature(Context context, long workorderId, long signatureId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "delete_signature"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/signature/[signatureId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .send();
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
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, "tasks/complete"), true));
    }

    /*-**************************************-*/
    /*-             Deliverables             -*/
    /*-**************************************-*/
    public static void getDeliverable(Context context, long workorderId, long deliverableId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableTransactionHandler.class)
                    .handlerParams(DeliverableTransactionHandler.pGet(workorderId, deliverableId))
                    .key((isSync ? "Sync/" : "") + "GetDeliverable/" + workorderId + "/" + deliverableId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/api/rest/v1/workorder/[workorderId]/deliverables/[deliverableId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the deliverable details
    public static void uploadDeliverable(Context context, String filePath, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable file");
        StoredObject upFile = StoredObject.put(App.getProfileId(), "TempFile", filePath, new File(filePath), "uploadTemp.dat");
        uploadDeliverable(context, upFile, filename, photoDescription, workorderId, uploadSlotId);
    }

    public static void uploadDeliverable(Context context, InputStream inputStream, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable uri");
        StoredObject upFile = StoredObject.put(App.getProfileId(), "TempFile", filename, inputStream, "uploadTemp.dat");
        uploadDeliverable(context, upFile, filename, photoDescription, workorderId, uploadSlotId);
    }

    public static void uploadDeliverable(Context context, StoredObject upFile, String filename, String photoDescription, long workorderId, long uploadSlotId) {
        Log.v(TAG, "uploadDeliverable uri");

        if (upFile.isFile() && upFile.getFile() != null) {
            if (upFile.getFile().length() > 100000000) { // 100 MB?
                StoredObject.delete(upFile);
                ToastClient.toast(context, "File is too long: " + filename, Toast.LENGTH_LONG);
                return;
            }
        }

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .timingKey("POST/api/rest/v1/workorder/[workorderId]/deliverables")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .multipartField("note", (photoDescription == null ? "" : misc.escapeForURL(photoDescription)))
//                    .body("note=" + (photoDescription == null ? "" : misc.escapeForURL(photoDescription)))
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pUploadDeliverable(workorderId, uploadSlotId, filename))
                    .useAuth(true)
                    .request(builder)
                    .setWifiRequired(App.get().onlyUploadWithWifi())
                    .setTrack(true)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns deliverable list
    public static void deleteDeliverable(Context context, long workorderId, long workorderUploadId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableTransactionHandler.class)
                    .handlerParams(DeliverableTransactionHandler.pChange(workorderId))
                    .useAuth(true)
                    .key("Workorder/DeleteDeliverable/" + workorderId + "/" + workorderUploadId)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("DELETE")
                                    .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/deliverables/[workorderUploadId]")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + workorderUploadId))
                    .send();
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
                    .timingKey("GET/api/rest/v1/workorder/[workorderId]/messages")
                    .path("/api/rest/v1/workorder/" + workorderId + "/messages");

            if (isRead) {
                builder.urlParams("?mark_read=1");
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pMessageList(workorderId, isRead))
                    .key((isSync ? "Sync/" : "") + "WorkorderMessageList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(builder)
                    .send();
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
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "create_discount"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .timingKey("POST/api/rest/v1/workorder/[workorderId]/discount")
                            .path("/api/rest/v1/workorder/" + workorderId + "/discount")
                            .body("description=" + misc.escapeForURL(description) + "&amount=" + misc.escapeForURL(price + "")))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns a discount list
    public static void deleteDiscount(Context context, long workorderId, long discountId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "delete_discount"))
                    .key("Workorder/DeleteDiscount/" + workorderId + "/" + discountId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/discounts/[discountId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/discounts/" + discountId))
                    .send();
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
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "create_expense"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .timingKey("POST/api/rest/v1/workorder/[workorderId]/expense")
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense")
                            .body(("description=" + misc.escapeForURL(description)
                                    + "&price=" + misc.escapeForURL(price + "")
                                    + "&category_id=" + category.getId()).trim()))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the expense
    public static void deleteExpense(Context context, long workorderId, long expenseId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "delete_expense"))
                    .key("Workorder/DeleteExpense/" + workorderId + "/" + expenseId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/expense/[expenseId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId))
                    .send();
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
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate, int numberOfDevices) {
        context.startService(
                action(context, workorderId, "POST", "log", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate)
                                + "&noOfDevices=" + numberOfDevices,
                        "POST/api/rest/v1/workorder/[workorderId]/log",
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate) {
        context.startService(
                action(context, workorderId, "POST", "log/" + loggedHoursId, null,
                        HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate),
                        "POST/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]",
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pTimeLog(workorderId), true));
    }

    // returns details
    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        context.startService(
                action(context, workorderId, "POST", "log/" + loggedHoursId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                        "startDate=" + ISO8601.fromUTC(startDate)
                                + "&endDate=" + ISO8601.fromUTC(endDate)
                                + "&noOfDevices=" + numberOfDevices,
                        "POST/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]",
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pTimeLog(workorderId), true));
    }

    // returns details
    public static void deleteTimeLog(Context context, long workorderId, long loggedHoursId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "DELETE_LOG"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/log/[loggedHoursId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId))
                    .send();
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
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCreateShipment(workorderId, description,
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
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCreateShipment(workorderId,
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
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCompleteShipmentTask(workorderId, shipmentId, taskId), true);
    }

    //    public static void postShipment(Context context, long workorderId, long shipmentId, String description, boolean isToSite,
//                                    String carrier, String carrierName, String trackingNumber) {
//        try {
//            WebTransactionBuilder.builder(context)
//                    .priority(Priority.HIGH)
//                    .handler(WorkorderTransactionHandler.class)
//                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "create_shipment"))
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
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAction(workorderId, "delete_shipment"))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .timingKey("DELETE/api/rest/v1/workorder/[workorderId]/shipments/[shipmentId]")
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId))
                    .send();
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


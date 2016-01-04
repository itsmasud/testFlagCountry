package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;

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
                            .path("/api/rest/v1/workorder/" + workorderId + "/details"))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void list(Context context, String selector, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pList(page, selector))
                    .key((isSync ? "Sync/" : "") + "WorkorderList/" + selector + "/" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + selector)
                            .urlParams("?page=" + page))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void listAlerts(Context context, long workorderId, boolean isRead, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pAlertList(workorderId))
                    .key((isSync ? "Sync/" : "") + "WorkorderAlertList/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/notifications"))
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
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    public static void action(Context context, long workorderId, String action, String params,
                              String contentType, String body) {
        context.startService(
                action(context, workorderId, action, params, contentType, body,
                        WorkorderTransactionHandler.class,
                        WorkorderTransactionHandler.pAction(workorderId, action)));
    }

    public static Intent action(Context context, long workorderId, String action, String params,
                                String contentType, String body,
                                Class<? extends WebTransactionHandler> clazz,
                                byte[] handlerParams) {
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", action);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
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

            return WebTransactionBuilder.builder(context)
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
                            _action.toByteArray()))
                    .makeIntent();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static void actionCompleteTask(Context context, long workorderId, long taskId) {
        action(context, workorderId, "tasks/complete/" + taskId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "");
    }

    public static void actionComplete(Context context, long workorderId) {
        action(context, workorderId, "complete", null, null, null);
    }

    public static void actionIncomplete(Context context, long workorderId) {
        action(context, workorderId, "incomplete", null, null, null);
    }

    public static void actionReportProblem(Context context, long workorderId, String explanation, ReportProblemType type) {
        if (misc.isEmptyOrNull(explanation)) {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "type=" + type.value);
        } else {
            action(context, workorderId, "report-problem", null,
                    HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                    "explanation=" + explanation
                            + "&type=" + type.value);
        }
    }

    public static void actionCheckin(Context context, long workorderId) {
        action(context, workorderId, "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + ISO8601.now());
    }

    public static void actionCheckin(Context context, long workorderId, Location location) {
        action(context, workorderId, "checkin", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkin_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude());
    }

    public static void actionCheckout(Context context, long workorderId) {
        action(context, workorderId, "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + ISO8601.now());
    }

    public static void actionCheckout(Context context, long workorderId, Location location) {
        action(context, workorderId, "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "checkout_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude());
    }

    public static void actionCheckout(Context context, long workorderId, int deviceCount) {
        action(context, workorderId, "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + ISO8601.now());
    }

    public static void actionCheckout(Context context, long workorderId, int deviceCount, Location location) {
        action(context, workorderId, "checkout", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "device_count=" + deviceCount
                        + "&checkout_time=" + ISO8601.now()
                        + "&gps_lat=" + location.getLatitude()
                        + "&gps_lon=" + location.getLongitude());
    }

    public static void actionClosingNotes(Context context, long workorderId, String closingNotes) {
        action(context, workorderId, "closing-notes", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "notes=" + (closingNotes == null ? "" : misc.escapeForURL(closingNotes)));
    }

    public static void actionAcknowledgeHold(Context context, long workorderId) {
        action(context, workorderId, "acknowledge-hold", null, null, null);
    }

    public static void actionCounterOffer(Context context, long workorderId, boolean expires,
                                          String reason, int expiresAfterInSecond, Pay pay,
                                          Schedule schedule, Expense[] expenses) {
        context.startService(
                actionCounterOfferIntent(context, workorderId, expires, reason, expiresAfterInSecond,
                        pay, schedule, expenses)
        );
    }

    public static Intent actionCounterOfferIntent(Context context, long workorderId, boolean expires,
                                                  String reason, int expiresAfterInSecond, Pay pay,
                                                  Schedule schedule, Expense[] expenses) {
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

        return action(context, workorderId, "counter_offer", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                payload,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pCounterOffer(workorderId, expires, reason,
                        expiresAfterInSecond, pay, schedule, expenses));
    }

    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        context.startService(
                actionRequestIntent(context, workorderId, expireInSeconds));
    }

    public static Intent actionRequestIntent(Context context, long workorderId, long expireInSeconds) {
        String body = null;

        if (expireInSeconds != -1) {
            body = "expiration=" + expireInSeconds;
        }

        return action(context, workorderId, "request", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                body,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionRequest(workorderId, expireInSeconds));
    }

    public static void actionConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        Intent intent = actionConfirmAssignmentIntent(context, workorderId, startTimeIso8601, endTimeIso8601);

        context.startService(intent);
    }

    public static Intent actionConfirmAssignmentIntent(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        return action(context, workorderId, "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "start_time=" + startTimeIso8601 + "&end_time=" + endTimeIso8601,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pAssignment(workorderId, startTimeIso8601, endTimeIso8601));
    }

    public static void actionDecline(Context context, long workorderId) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "");
    }

    public static void actionReady(Context context, long workorderId) {
        context.startService(actionReadyIntent(context, workorderId));
    }

    public static Intent actionReadyIntent(Context context, long workorderId) {
        return action(context, workorderId, "ready", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "",
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pAction(workorderId, "ready")
        );
    }


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
                    .path("/api/rest/v1/workorder/" + workorderId + "/rate");

            if (body != null) {
                http.body(body);

                http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);
            }

            return WebTransactionBuilder.builder(context)
                    .priority(Priority.LOW)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pRating(satisfactionRating, scopeRating, respectRating,
                            respectComment, recommendBuyer, otherComments, workorderId))
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
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

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

    // TODO make sure this works
    public static void addSignatureSvgTask(Context context, long workorderId, long taskId, String name, String svg) {
        action(context, workorderId, "tasks/complete/" + taskId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "print_name=" + misc.escapeForURL(name)
                        + "&signature_json=" + svg);
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
                            .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

//    public static void downloadDeliverable(Context context, long workorderId, long deliverableId, String url, boolean isSync) {
//        try {
//            WebTransactionBuilder.builder(context)
//                    .priority(Priority.HIGH)
//                    .handler(DeliverableTransactionHandler.class)
//                    .handlerParams(DeliverableTransactionHandler.pDownload(workorderId, deliverableId, url))
//                    .key((isSync ? "Sync/" : "") + "DeliverableDownload/" + workorderId + "/" + deliverableId)
//                    .isSyncCall(isSync)
//                    .request(new HttpJsonBuilder()
//                            .method("GET")
//                            .path(url))
//                    .send();
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
//    }

    public static void uploadDeliverable(Context context, String filePath, String filename, long workorderId, long uploadSlotId) {
        StoredObject upFile = StoredObject.put(App.getProfileId(), "TempFile", filePath, new File(filePath), "uploadTemp.dat");
        Resources res = context.getResources();
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .notify(res.getString(R.string.app_name),
                            res.getString(R.string.notification_start_body_uploading, filename),
                            res.getString(R.string.notification_start_body_uploading, filename),
                            R.drawable.ic_notification,
                            res.getString(R.string.notification_success_title),
                            res.getString(R.string.notification_success_body_uploading, filename),
                            res.getString(R.string.notification_success_body_uploading, filename),
                            R.drawable.ic_notification,
                            res.getString(R.string.notification_failed_title),
                            res.getString(R.string.notification_failed_body_uploading, filename),
                            res.getString(R.string.notification_failed_body_uploading, filename),
                            R.drawable.ic_notification)
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pUploadDeliverable(workorderId, uploadSlotId, filename))
                    .useAuth(true)
                    .request(builder)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


    public static void uploadDeliverable(Context context, Uri uri, String filename, long workorderId, long uploadSlotId) {
        try {
            Resources res = context.getResources();
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, uri)
                    .notify(res.getString(R.string.app_name),
                            res.getString(R.string.notification_start_body_uploading, filename),
                            res.getString(R.string.notification_start_body_uploading, filename),
                            R.drawable.ic_notification,
                            res.getString(R.string.notification_success_title),
                            res.getString(R.string.notification_success_body_uploading, filename),
                            res.getString(R.string.notification_success_body_uploading, filename),
                            R.drawable.ic_notification,
                            res.getString(R.string.notification_failed_title),
                            res.getString(R.string.notification_failed_body_uploading, filename),
                            res.getString(R.string.notification_failed_body_uploading, filename),
                            R.drawable.ic_notification)
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pUploadDeliverable(workorderId, uploadSlotId, filename))
                    .useAuth(true)
                    .request(builder)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

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
                    .path("/api/rest/v1/workorder/" + workorderId + "/messages");

            if (isRead) {
                builder.urlParams("?mark_read=1");
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pMessageList(workorderId))
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
                            .path("/api/rest/v1/workorder/" + workorderId + "/discount")
                            .body("description=" + misc.escapeForURL(description)
                                    + "&amount=" + misc.escapeForURL(price + "")))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

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
                            .path("/api/rest/v1/workorder/" + workorderId + "/discounts/" + discountId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Expense             -*/
    /*-*********************************-*/
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
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense")
                            .body(("description=" + misc.escapeForURL(description)
                                    + "&price=" + misc.escapeForURL(price + "")
                                    + "&category_id=" + category.getId()).trim()))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

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
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-***********************************-*/
    /*-             Time Logs             -*/
    /*-***********************************-*/
    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate) {
        action(context, workorderId, "log", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate));
    }

    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate, int numberOfDevices) {
        action(context, workorderId, "log", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate)
                        + "&noOfDevices=" + numberOfDevices);
    }

    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate) {
        action(context, workorderId, "log/" + loggedHoursId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate));
    }

    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        action(context, workorderId, "log/" + loggedHoursId, null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate)
                        + "&noOfDevices=" + numberOfDevices);
    }

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
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-***********************************-*/
    /*-             Shipments             -*/
    /*-***********************************-*/
    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber) {
        context.startService(postShipmentIntent(context, workorderId, description, isToSite, carrier, carrierName, trackingNumber));
    }

    public static Intent postShipmentIntent(Context context, long workorderId, String description, boolean isToSite,
                                            String carrier, String carrierName, String trackingNumber) {
        return action(context, workorderId, "shipments", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=Other"
                        + "&carrier_name=" + (carrierName == null ? misc.escapeForURL(carrier) : misc.escapeForURL(carrierName))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber),
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCreateShipment(workorderId,
                        description, isToSite, carrier, carrierName, trackingNumber, -1));
    }

    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber, long taskId) {
        context.startService(postShipmentIntent(context, workorderId, description, isToSite, carrier, carrierName, trackingNumber, taskId));
    }

    public static Intent postShipmentIntent(Context context, long workorderId, String description, boolean isToSite,
                                            String carrier, String carrierName, String trackingNumber, long taskId) {
        return action(context, workorderId, "shipments", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=Other"
                        + "&carrier_name=" + (carrierName == null ? misc.escapeForURL(carrier) : misc.escapeForURL(carrierName))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber)
                        + "&task_id=" + taskId,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCreateShipment(workorderId,
                        description, isToSite, carrier, carrierName, trackingNumber, taskId));
    }

    public static void actionCompleteShipmentTask(Context context, long workorderId, long shipmentId, long taskId) {
        context.startService(actionCompleteShipmentTaskIntent(context, workorderId, shipmentId, taskId));
    }

    public static Intent actionCompleteShipmentTaskIntent(Context context, long workorderId, long shipmentId, long taskId) {
        return action(context, workorderId, "tasks/complete/" + taskId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "shipment_id=" + shipmentId,
                WorkorderTransactionHandler.class,
                WorkorderTransactionHandler.pActionCompleteShipmentTask(workorderId, shipmentId, taskId));
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
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void actionCompleteSignatureTask(Context context, long workorderId, long taskId, String printName, String signatureJson) {
        action(context, workorderId, "tasks/complete/" + taskId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "print_name=" + misc.escapeForURL(printName)
                        + "&signature_json=" + signatureJson);
    }
}


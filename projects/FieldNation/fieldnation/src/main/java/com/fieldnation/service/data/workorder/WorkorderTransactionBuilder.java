package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.location.Location;

import com.fieldnation.data.transfer.WorkorderTransfer;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.NullWebTransactionHandler;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class WorkorderTransactionBuilder implements WorkorderConstants {

    public static void getWorkorder(Context context, long workorderId, boolean isSync) {
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
            ex.printStackTrace();
        }
    }

    public static void getWorkorderList(Context context, String selector, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderListTransactionHandler.class)
                    .handlerParams(WorkorderListTransactionHandler.generateParams(page, selector))
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
            ex.printStackTrace();
        }
    }

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
            ex.printStackTrace();
        }
    }

    public static void getBundle(Context context, long bundleId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(BundleTransactionHandler.class)
                    .handlerParams(BundleTransactionHandler.pBundle(bundleId))
                    .key((isSync ? "Sync/" : "") + "GetBundle/" + bundleId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
            ex.printStackTrace();
        }
    }

    public static void listDeliverables(Context context, long workorderId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableTransactionHandler.class)
                    .handlerParams(DeliverableTransactionHandler.pList(workorderId))
                    .key((isSync ? "Sync/" : "") + "ListDeliverables/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/deliverables"))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void downloadDeliverable(Context context, long workorderId, long deliverableId, String url, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableTransactionHandler.class)
                    .handlerParams(DeliverableTransactionHandler.pDownload(workorderId, deliverableId, url))
                    .key((isSync ? "Sync/" : "") + "DeliverableDownload/" + workorderId + "/" + deliverableId)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .method("GET")
                            .path(url))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postDeliverable(Context context, String filePath, String filename, long workorderId, long uploadSlotId) {
        StoredObject upFile = StoredObject.put(context, "TempFile", filePath, new File(filePath));

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableTransactionHandler.class)
                    .handlerParams(DeliverableTransactionHandler.pChange(workorderId))
                    .useAuth(true)
                    .request(builder)
                    .transform(Transform.makeTransformQuery(
                            "Workorder",
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeUploadDeliverable(uploadSlotId, filename).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postSignatureJson(Context context, long workorderId, String name, String json) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature")
                            .body("signatureFormat=json"
                                    + "&printName=" + misc.escapeForURL(name)
                                    + "&signature=" + json))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeAddSignatureTransfer(name).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postSignatureJsonTask(Context context, long workorderId, long taskId, String name, String json) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId)
                            .body("print_name=" + misc.escapeForURL(name)
                                    + "&signature_json=" + json))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCompletingTaskTransfer("signature", taskId, name).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void action(Context context, long workorderId, String action, String params, String contentType, String body) {
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

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth(true)
                    .key("Workorder/" + workorderId + "/" + action)
                    .request(http)
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void actionComplete(Context context, long workorderId) {
        action(context, workorderId, "complete", null, null, null);
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
            }
            json.append("]");

            payload += "&expenses=" + json.toString();
        }

        action(context, workorderId, "counter_offer", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                payload);
    }

    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        String body = null;

        if (expireInSeconds != -1) {
            body = "expiration=" + expireInSeconds;
        }

        action(context, workorderId, "request", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, body);
    }

    public static void actionDecline(Context context, long workorderId) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED, "");
    }

    public static void actionReady(Context context, long workorderId) {
        action(context, workorderId, "ready", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "");
    }

    public static void actionConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        action(context, workorderId, "assignment", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "start_time=" + startTimeIso8601 + "&end_time=" + endTimeIso8601);
    }

    public static void actionWithdrawRequest(Context context, long workorderId) {
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", "withdraw-request");

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
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
            ex.printStackTrace();
        }
    }


    public static void deleteDeliverable(Context context, long workorderId, long workorderUploadId, String filename) {
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
                    .transform(
                            Transform.makeTransformQuery(
                                    PSO_WORKORDER,
                                    workorderId,
                                    "merges",
                                    WorkorderTransfer.makeDeleteDeliverable(workorderUploadId, filename).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void postCustomField(Context context, long workorderId, long customFieldId, String value) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                                    .method("POST")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/custom-fields/" + customFieldId)
                                    .body("value=" + misc.escapeForURL(value)))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postMessagesRead(Context context, long workorderId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .key("Workorder/MessagesRead/" + workorderId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/messages")
                            .body("?mark_read=1"))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getMessages(Context context, long workorderId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .key("Workorder/Messages/" + workorderId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/messages"))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postExpense(Context context, long workorderId, String description, double price,
                                   ExpenseCategory category) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
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
            ex.printStackTrace();
        }
    }

    public static void deleteExpense(Context context, long workorderId, long expenseId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .key("Workorder/DeleteExpense/" + workorderId + "/" + expenseId)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postTimeLog(Context context, long workorderId, long startDate, long endDate) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/log")
                            .body("startDate=" + ISO8601.fromUTC(startDate)
                                    + "&endDate=" + ISO8601.fromUTC(endDate)))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId)
                            .body("startDate=" + ISO8601.fromUTC(startDate)
                                    + "&endDate=" + ISO8601.fromUTC(endDate)))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId)
                            .body("startDate=" + ISO8601.fromUTC(startDate)
                                    + "&endDate=" + ISO8601.fromUTC(endDate)
                                    + "&noOfDevices=" + numberOfDevices))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteTimeLog(Context context, long workorderId, long loggedHoursId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments")
                            .body("description=" + misc.escapeForURL(description)
                                    + "&direction=" + (isToSite ? "to_site" : "from_site")
                                    + "&carrier=" + carrier
                                    + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                                    + "&tracking_number=" + misc.escapeForURL(trackingNumber)))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postShipment(Context context, long workorderId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber, long taskId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments")
                            .body("description=" + misc.escapeForURL(description)
                                    + "&direction=" + (isToSite ? "to_site" : "from_site")
                                    + "&carrier=" + carrier
                                    + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                                    + "&tracking_number=" + misc.escapeForURL(trackingNumber)
                                    + "&task_id=" + taskId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postShipment(Context context, long workorderId, long shipmentId, String description, boolean isToSite,
                                    String carrier, String carrierName, String trackingNumber) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId)
                            .body("description=" + misc.escapeForURL(description)
                                    + "&direction=" + (isToSite ? "to_site" : "from_site")
                                    + "&carrier=" + carrier
                                    + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                                    + "&tracking_number=" + misc.escapeForURL(trackingNumber)))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteShipment(Context context, long workorderId, long shipmentId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

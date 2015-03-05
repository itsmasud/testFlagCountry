package com.fieldnation.rpc.webclient;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.ResultReceiver;

import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

public class WorkorderWebService extends WebService implements WebServiceConstants {

    public WorkorderWebService(Context context, String username, String authToken, ResultReceiver callback) {
        super(context, username, authToken, callback);
    }

    // workorders

    public Intent getList(int resultCode, int page, WorkorderDataSelector selector, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/workorder/" + selector.getCall(), "?page=" + page, allowCache);
    }

//    public Intent getRequested(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/requested", "?page=" + page, allowCache);
//    }
//
//    public Intent getAvailable(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/available", "?page=" + page, allowCache);
//    }
//
//    public Intent getPendingApproval(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/pending_approval", "?page=" + page, allowCache);
//    }
//
//    public Intent getAssigned(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/assigned", "?page=" + page, allowCache);
//    }
//
//    public Intent getCompleted(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/completed", "?page=" + page, allowCache);
//    }
//
//    public Intent getCanceled(int resultCode, int page, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/canceled", "?page=" + page, allowCache);
//    }

    public Intent getDetails(int resultCode, long workorderId, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/details", allowCache);
    }

    public Intent decline(int resultCode, long workorderId) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/decline", null, "",
                "application/x-www-form-urlencoded", false);
    }

    // interactions
    public Intent request(int resultCode, long workorderId, long expireInSeconds) {
        if (expireInSeconds == -1)
            return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null, "",
                    "application/x-www-form-urlencoded", false);

        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null,
                "expiration=" + expireInSeconds, "application/x-www-form-urlencoded", false);
    }

//    public Intent withdrawRequest(int resultCode, long workorderId) {
//        return httpDelete(resultCode, "/api/rest/v1/workorder/" + workorderId + "/withdraw-request", null, false);
//    }
//
//    public Intent deleteRequest(int resultCode, long workorderId) {
//        return httpDelete(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null, false);
//    }

    public Intent confirmAssignment(int resultCode, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/assignment", null,
                "start_time=" + startTimeIso8601 + "&end_time=" + endTimeIso8601, "application/x-www-form-urlencoded",
                false);
    }

//    public Intent cancelAssignment(int resultCode, long workorderId, CancelCategory category) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/cancel-assignment", null,
//                "cancel_category=" + category.getId(), "application/x-www-form-urlencoded", false);
//    }
//
//    public Intent cancelAssignment(int resultCode, long workorderId, CancelCategory category, String cancelReason) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/cancel-assignment", null,
//                "cancel_category=" + category.getId() + "&cancel_reason=" + misc.escapeForURL(cancelReason),
//                "application/x-www-form-urlencoded", false);
//    }

    public Intent ready(int resultCode, long workorderId, String cancelCategory, String cancelReason, boolean allowCache) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/ready", null, "",
                "application/x-www-form-urlencoded", allowCache);
    }

    public Intent checkin(int resultCode, long workorderId) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkin", null,
                "checkin_time=" + ISO8601.now(), "application/x-www-form-urlencoded", false);
    }

//    public Intent checkin(int resultCode, long workorderId, double gps_lat, double gps_lon) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkin", null,
//                "checkin_time=" + ISO8601.now() + "&gps_lat=" + gps_lat + "&gps_lon=" + gps_lon, "application/x-www-form-urlencoded", false);
//    }

    public Intent checkin(int resultCode, long workorderId, Location location) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkin", null,
                "checkin_time=" + ISO8601.now() + "&gps_lat=" + location.getLatitude() + "&gps_lon=" + location.getLongitude(), "application/x-www-form-urlencoded", false);
    }

    public Intent checkout(int resultCode, long workorderId) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
                "checkout_time=" + ISO8601.now(), "application/x-www-form-urlencoded", false);
    }

//    public Intent checkout(int resultCode, long workorderId, double gps_lat, double gps_lon) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
//                "checkout_time=" + ISO8601.now() + "&gps_lat=" + gps_lat + "&gps_lon=" + gps_lon, "application/x-www-form-urlencoded", false);
//    }

    public Intent checkout(int resultCode, long workorderId, Location location) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
                "checkout_time=" + ISO8601.now() + "&gps_lat=" + location.getLatitude() + "&gps_lon=" + location.getLongitude(), "application/x-www-form-urlencoded", false);
    }

//    public Intent checkout(int resultCode, long workorderId, int deviceCount, double gps_lat, double gps_lon) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
//                "device_count=" + deviceCount + "&checkout_time=" + ISO8601.now() + "&gps_lat=" + gps_lat + "&gps_lon=" + gps_lon, "application/x-www-form-urlencoded", false);
//    }

    public Intent checkout(int resultCode, long workorderId, int deviceCount, Location location) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
                "device_count=" + deviceCount + "&checkout_time=" + ISO8601.now() + "&gps_lat=" + location.getLatitude() + "&gps_lon=" + location.getLongitude(), "application/x-www-form-urlencoded", false);
    }

    public Intent checkout(int resultCode, long workorderId, int deviceCount) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkout", null,
                "checkout_time=" + ISO8601.now() + "&device_count=" + deviceCount, "application/x-www-form-urlencoded", false);
    }

    public Intent closingNotes(int resultCode, long workorderId, String notes) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/closing-notes", null,
                "notes=" + (notes == null ? "" : misc.escapeForURL(notes)), "application/x-www-form-urlencoded", false);
    }

    public Intent acknowledgeHold(int resultCode, long workorderId) {
        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/acknowledge-hold", null, false);
    }

    public Intent complete(int resultCode, long workorderId) {
        return httpRead(resultCode, "POST", "api/rest/v1/workorder/" + workorderId + "/complete", null, false);
    }

    // custom fields
    public Intent setCustomField(int resultCode, long workorderId, long customFieldId, String value) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/custom-fields/" + customFieldId, null,
                "value=" + misc.escapeForURL(value), "application/x-www-form-urlencoded", false);
    }

    // messages
    public Intent markMessagesRead(int resultCode, long workorderId) {
        return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages",
                "?mark_read=1", false);
    }

    public Intent listMessages(int resultCode, long workorderId, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages", allowCache);
    }

    public Intent addMessage(int resultCode, long workorderId, String message) {
        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages/new", null,
                "message=" + misc.escapeForURL(message), "application/x-www-form-urlencoded", false);
    }

//    public Intent addMessage(int resultCode, long workorderId, long messageId, String message) {
//        return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages/" + messageId + "/reply",
//                null, "message=" + misc.escapeForURL(message), "application/x-www-form-urlencoded", false);
//    }
//
//    // expenses
//    public Intent listExpenses(int resultCode, long workorderId, boolean allowCache) {
//        return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/expenses", null, allowCache);
//    }

    public Intent addExpense(int resultCode, long workorderId, String description, double price,
                             ExpenseCategory category) {
        return httpPost(
                resultCode,
                "/api/rest/v1/workorder/" + workorderId + "/expense",
                null,
                ("description=" + misc.escapeForURL(description)
                        + "&price=" + misc.escapeForURL(price + "")
                        + "&category_id=" + category.getId()).trim(),
                "application/x-www-form-urlencoded", false);
    }

    public Intent deleteExpense(int resultCode, long workorderId, long expenseId) {
        return httpDelete(resultCode, "/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId, null, false);
    }

//    public Intent listExpenseCategories(int resultCode, boolean allowCache) {
//        return httpGet(resultCode, "api/rest/v1/workorder/expense/categories", null, allowCache);
//    }

    // time
    public Intent logTime(int resultCode, long workorderId, long startDate, long endDate) {
        return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/log", null,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate),
                "application/x-www-form-urlencoded", false);
    }

    public Intent logTime(int resultCode, long workorderId, long startDate, long endDate, int numberOfDevices) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/log",
                null,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate)
                        + "&noOfDevices=" + numberOfDevices,
                "application/x-www-form-urlencoded", false);
    }

    public Intent updateLogTime(int resultCode, long workorderId, long loggedHoursId, long startDate, long endDate) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId,
                null,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate),
                "application/x-www-form-urlencoded", false);
    }

    public Intent updateLogTime(int resultCode, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId,
                null,
                "startDate=" + ISO8601.fromUTC(startDate)
                        + "&endDate=" + ISO8601.fromUTC(endDate)
                        + "&noOfDevices=" + numberOfDevices,
                "application/x-www-form-urlencoded", false);
    }

    public Intent deleteLogTime(int resultCode, long workorderId, long loggedHoursId) {
        return httpDelete(resultCode,
                "api/rest/v1/workorder/" + workorderId + "/log/" + loggedHoursId,
                "", false);
    }

    // shipments
    public Intent addShipmentDetails(int resultCode, long workorderId, String description, boolean isToSite,
                                     String carrier, String carrierName, String trackingNumber) {
        return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/shipments", null,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=" + carrier
                        + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber),
                "application/x-www-form-urlencoded", false);
    }

    public Intent addShipmentDetails(int resultCode, long workorderId, String description, boolean isToSite,
                                     String carrier, String carrierName, String trackingNumber, long taskId) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/shipments",
                null,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=" + carrier
                        + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber)
                        + "&task_id=" + taskId,
                "application/x-www-form-urlencoded", false);
    }

    public Intent deleteShipment(int resultCode, long workorderId, long shipmentId) {
        return httpDelete(resultCode, "api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId, null, false);
    }

    public Intent setShipment(int resultCode, long workorderId, long shipmentId, String description, boolean isToSite,
                              String carrier, String carrierName, String trackingNumber) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId,
                null,
                "description=" + misc.escapeForURL(description)
                        + "&direction=" + (isToSite ? "to_site" : "from_site")
                        + "&carrier=" + carrier
                        + (carrierName == null ? "" : ("&carrier_name=" + misc.escapeForURL(carrierName)))
                        + "&tracking_number=" + misc.escapeForURL(trackingNumber),
                "application/x-www-form-urlencoded", false);
    }

    // bundles
    public Intent getBundle(int resultCode, long bundleId, boolean allowCache) {
        return httpGet(resultCode, "api/rest/v1/workorder/bundle/" + bundleId, allowCache);
    }

    // counter offers
/*
    public Intent setFixedCounterOffer(int resultCode, long workorderId, double fixedTotalAmount, String explanation,
                                       boolean expire, int expireAfterMinutes) {
        if (expire) {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=fixed&fixedTotalAmount=" + fixedTotalAmount + "&explanation=" + explanation + "&expire=" + expire + "&expireAfterMinutes" + expireAfterMinutes,
                    "application/x-www-form-urlencoded", false);
        } else {
            return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/counter_offer", null,
                    "payBasis=fixed&fixedTotalAmount=" + fixedTotalAmount + "&explanation=" + explanation,
                    "application/x-www-form-urlencoded", false);
        }
    }

    public Intent setHourlyCounterOffer(int resultCode, long workorderId, double perHourRate, double maxHours,
                                        String explanation, boolean expire, int expireAfterMinutes) {
        if (expire) {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=per_hour&perHourRate=" + perHourRate + "&maxHours=" + maxHours + "&explanation=" + explanation + "&expire=" + expire + "&expireAfterMinutes" + expireAfterMinutes,
                    "application/x-www-form-urlencoded", false);
        } else {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=per_hour&perHourRate=" + perHourRate + "&maxHours=" + maxHours + "&explanation=" + explanation,
                    "application/x-www-form-urlencoded", false);
        }
    }

    public Intent setPerDeviceCounterOffer(int resultCode, long workorderId, double perDeviceRate, double maxDevice,
                                           String explanation, boolean expire, int expireAfterMinutes) {
        if (expire) {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=per_device&perDeviceRate=" + perDeviceRate + "&maxDeviceRate=" + maxDevice + "&explanation=" + explanation + "&expire=" + expire + "&expireAfterMinutes" + expireAfterMinutes,
                    "application/x-www-form-urlencoded", false);
        } else {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=per_device&perDeviceRate=" + perDeviceRate + "&maxDeviceRate=" + maxDevice + "&explanation=" + explanation,
                    "application/x-www-form-urlencoded", false);
        }
    }

    public Intent setBlendedCounterOffer(int resultCode, long workorderId, double perHourRate, double maxHours,
                                         double additionalHourRate, double additionalHours, String explanation, boolean expire,
                                         int expireAfterMinutes) {
        if (expire) {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=blended&perHourRate=" + perHourRate + "&maxHours=" + maxHours + "&additionalHourRate=" + additionalHourRate + "&additionalHours=" + additionalHours + "&explanation=" + explanation + "&expire=" + expire + "&expireAfterMinutes" + expireAfterMinutes,
                    "application/x-www-form-urlencoded", false);
        } else {
            return httpPost(
                    resultCode,
                    "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                    null,
                    "payBasis=blended&perHourRate=" + perHourRate + "&maxHours=" + maxHours
                            + "&additionalHourRate=" + additionalHourRate
                            + "&additionalHours=" + additionalHours
                            + "&explanation=" + explanation,
                    "application/x-www-form-urlencoded", false);
        }
    }
*/

    public Intent setCounterOffer(int resultCode, long workorderId, boolean expires, String reason, int expiresAfterInSecond, Pay pay, Schedule schedule, Expense[] expenses) {
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

        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/counter_offer",
                null,
                payload,
                "application/x-www-form-urlencoded", false);
    }

    // deliverables
    public Intent deleteDeliverable(int resultCode, long workorderId, long deliverableId) {
        return httpDelete(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableId, null,
                false);
    }

    public Intent uploadDeliverable(int resultCode, long workorderId, long deliverableSlotId, String localFilename) {
        if (deliverableSlotId <= 0) {
            return httpPostFile(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables",
                    null, "file", localFilename, null, null);
        }
        return httpPostFile(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableSlotId,
                null, "file", localFilename, null, null);
    }

    public Intent uploadDeliverable(int resultCode, long workorderId, long deliverableSlotId, Intent data, PendingIntent notificationIntent) {
        if (deliverableSlotId <= 0) {
            return httpPostFile(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables", null,
                    "file", data, null, notificationIntent);
        }
        return httpPostFile(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableSlotId,
                null, "file", data, null, notificationIntent);
    }


//    public Intent getDeliverableDetails(int resultCode, long workorderId, long deliverableId, boolean allowCache) {
//        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableId,
//                allowCache);
//    }
//
//    public Intent listDeliverables(int resultCode, long workorderId, boolean allowCache) {
//        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/deliverables", allowCache);
//    }

    // notifications
    public Intent listNotifications(int resultCode, long workorderId, boolean allowCache) {
        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/notifications", allowCache);
    }

    public Intent getTasks(int resultCode, long workorderId, boolean allowCache) {
        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/tasks", allowCache);
    }

    // discounts
    public Intent deleteDiscount(int resultCode, long workorderId, long discountId) {
        return httpDelete(resultCode, "api/rest/v1/workorder/" + workorderId + "/discounts/" + discountId, null, false);
    }

//    public Intent listDiscounts(int resultCode, long workorderId, boolean allowCache) {
//        return httpGet(resultCode, "api/rest/v1/workroder/" + workorderId + "/discounts", null, allowCache);
//    }

    public Intent addDiscount(int resultCode, long workorderId, double amount, String description) {
        return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/discount", null,
                "amount=" + amount + "&description=" + description, "application/x-www-form-urlencoded", false);
    }

    // tasks
    public Intent completeSignatureTaskJson(int resultCode, long workorderId, long taskId, String printName, String signatureJson) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId,
                null,
                "print_name=" + misc.escapeForURL(printName)
                        + "&signature_json=" + signatureJson,
                "application/x-www-form-urlencoded", false);

    }

    public Intent completeTask(int resultCode, long workorderId, long taskId) {
        return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId, null, "",
                "application/x-www-form-urlencoded", false);
    }

    public Intent completeShipmentTask(int resultCode, long workorderId, long shipmentId, long taskId) {
        return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId, null,
                "shipment_id=" + shipmentId, "application/x-www-form-urlencoded", false);
    }

    // signature collection
    public Intent addSignatureJson(int resultCode, long workorderId, String printName, String jsonSignature) {
        return httpPost(
                resultCode,
                "api/rest/v1/workorder/" + workorderId + "/signature",
                null,
                "signatureFormat=json"
                        + "&printName=" + misc.escapeForURL(printName)
                        + "&signature=" + jsonSignature,
                "application/x-www-form-urlencoded", false);
    }

//    public Intent listSignatures(int resultCode, long workorderId, boolean allowCache) {
//        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/signature", null, allowCache);
//    }

    public Intent getSignature(int resultCode, long workorderId, long signatureId, boolean allowCache) {
        return httpGet(resultCode, "api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId, null, allowCache);
    }

}

package com.fieldnation.rpc.client;

import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class WorkorderService extends WebService implements WebServiceConstants {

	public WorkorderService(Context context, String username, String authToken, ResultReceiver callback) {
		super(context, username, authToken, callback);
	}

	public Intent getRequested(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/requested", "?page=" + page, allowCache);
	}

	public Intent getAvailable(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/available", "?page=" + page, allowCache);
	}

	public Intent getPendingApproval(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/pending_approval", "?page=" + page, allowCache);
	}

	public Intent getAssigned(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/assigned", "?page=" + page, allowCache);
	}

	public Intent getCompleted(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/completed", "?page=" + page, allowCache);
	}

	public Intent getCanceled(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/canceled", "?page=" + page, allowCache);
	}

	public Intent getDetails(int resultCode, long workorderId, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/details", allowCache);
	}

	public Intent decline(int resultCode, long workorderId) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/decline", null, "",
				"application/x-www-form-urlencoded", false);
	}

	public Intent request(int resultCode, long workorderId, int expireInSeconds) {
		if (expireInSeconds == -1)
			return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null, "",
					"application/x-www-form-urlencoded", false);

		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null,
				"expiration=" + expireInSeconds, "application/x-www-form-urlencoded", false);
	}

	public Intent withdrawRequest(int resultCode, long workorderId) {
		return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/withdraw-request", false);
	}

	public Intent removeRequest(int resultCode, long workorderId) {
		return httpDelete(resultCode, "/api/rest/v1/workorder/" + workorderId + "/request", null, false);
	}

	public Intent confirmAssignment(int resultCode, long workorderId, long startTimeMilliseconds, long endTimeMilliseconds) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/assignment", null,
				"start_time=" + ISO8601.fromUTC(startTimeMilliseconds) + "&end_time=" + ISO8601.fromUTC(endTimeMilliseconds),
				"application/x-www-form-urlencoded", false);
	}

	public Intent cancelAssignment(int resultCode, long workorderId, CancelCategory category) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/cancel-assignment", null,
				"cancel_category=" + category.getId(), "application/x-www-form-urlencoded", false);
	}

	public Intent cancelAssignment(int resultCode, long workorderId, CancelCategory category, String cancelReason) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/cancel-assignment", null,
				"cancel_category=" + category.getId() + "&cancel_reason=" + misc.escapeForURL(cancelReason),
				"application/x-www-form-urlencoded", false);
	}

	public Intent ready(int resultCode, long workorderId, String cancelCategory, String cancelReason, boolean allowCache) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/ready", null, "",
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent checkin(int resultCode, long workorderId, long checkinTime) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkin_time=" + ISO8601.fromUTC(checkinTime), "application/x-www-form-urlencoded", false);
	}

	public Intent checkout(int resultCode, long workorderId, long checkoutTimeMilliseconds) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkout_time=" + ISO8601.fromUTC(checkoutTimeMilliseconds), "application/x-www-form-urlencoded", false);
	}

	public Intent closingNotes(int resultCode, long workorderId, String notes) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/closing-notes", null,
				"notes=" + misc.escapeForURL(notes), "application/x-www-form-urlencoded", false);
	}

	public Intent getMessages(int resultCode, long workorderId, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages", allowCache);
	}

	public Intent newMessage(int resultCode, long workorderId, String message) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages/new", null,
				"message=" + misc.escapeForURL(message), "application/x-www-form-urlencoded", false);
	}

	public Intent newMessage(int resultCode, long workorderId, long messageId, String message) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/messages/" + messageId + "/reply",
				null, "message=" + misc.escapeForURL(message), "application/x-www-form-urlencoded", false);
	}

	public Intent listExpenses(int resultCode, long workorderId, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/" + workorderId + "/expenses", null, allowCache);
	}

	public Intent addExpense(int resultCode, long workorderId, String description, double price) {
		return httpPost(resultCode, "/api/rest/v1/workorder/" + workorderId + "/expense", null,
				"description=" + misc.escapeForURL(description) + "&price=" + price,
				"application/x-www-form-urlencoded", false);
	}

	public Intent deleteExpense(int resultCode, long workorderId, long expenseId) {
		return httpDelete(resultCode, "/api/rest/v1/workorder/" + workorderId + "/expense/" + expenseId, null, false);
	}

	public Intent logTime(int resultCode, long workorderId, long startDate, long endDate) {
		return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/log", null,
				"startDate=" + ISO8601.fromUTC(startDate) + "&endDate=" + ISO8601.fromUTC(endDate),
				"application/x-www-form-urlencoded", false);
	}

	public Intent logTime(int resultCode, long workorderId, long startDate, long endDate, int numberOfDevices) {
		return httpPost(
				resultCode,
				"api/rest/v1/workorder/" + workorderId + "/log",
				null,
				"startDate=" + ISO8601.fromUTC(startDate) + "&endDate=" + ISO8601.fromUTC(endDate) + "&noOfDevices=" + numberOfDevices,
				"application/x-www-form-urlencoded", false);
	}

	public Intent logTime(int resultCode, long workorderId, long startDate, long endDate, boolean isOnSiteWork) {
		return httpPost(
				resultCode,
				"api/rest/v1/workorder/" + workorderId + "/log",
				null,
				"startDate=" + ISO8601.fromUTC(startDate) + "&endDate=" + ISO8601.fromUTC(endDate) + "&hoursType=" + (isOnSiteWork ? "0" : "1"),
				"application/x-www-form-urlencoded", false);
	}

	public Intent acknowledgeHold(int resultCode, long workorderId) {
		return httpPost(resultCode, "api/rest/v1/workorder/" + workorderId + "/acknowledge-hold", null, "",
				"application/x-www-form-urlencoded", false);
	}

	public Intent getExpenseCategories(int resultCode, boolean allowCache) {
		return httpGet(resultCode, "api/rest/v1/workorder/expense/categories", null, allowCache);
	}

	public Intent addShipmentDetails(int resultCode, long workorderId, String description, boolean isToSite,
			String carrier, String carrierName, String trackingNumber) {
		return httpPost(
				resultCode,
				"api/rest/v1/workorder/" + workorderId + "/shipments",
				null,
				"description=" + misc.escapeForURL(description) + "&direction=" + (isToSite ? "to_site" : "from_site") + "&carrier=" + carrier + (carrierName == null ? "" : ("&carrier_name=" + carrierName)) + "&tracking_number=" + trackingNumber,
				"application/x-www-form-urlencoded", false);
	}

	public Intent addShipmentDetails(int resultCode, long workorderId, String description, boolean isToSite,
			String carrier, String carrierName, String trackingNumber, long taskId) {
		return httpPost(
				resultCode,
				"api/rest/v1/workorder/" + workorderId + "/shipments",
				null,
				"description=" + misc.escapeForURL(description) + "&direction=" + (isToSite ? "to_site" : "from_site") + "&carrier=" + carrier + (carrierName == null ? "" : ("&carrier_name=" + carrierName)) + "&tracking_number=" + trackingNumber + "&task_id=" + taskId,
				"application/x-www-form-urlencoded", false);
	}

	public Intent editShipment(int resultCode, long workorderId, long shipmentId, String description, boolean isToSite,
			String carrier, String carrierName, String trackingNumber) {
		return httpPost(
				resultCode,
				"api/rest/v1/workorder/" + workorderId + "/shipments/" + shipmentId,
				null,
				"description=" + misc.escapeForURL(description) + "&direction=" + (isToSite ? "to_site" : "from_site") + "&carrier=" + carrier + (carrierName == null ? "" : ("&carrier_name=" + carrierName)) + "&tracking_number=" + trackingNumber,
				"application/x-www-form-urlencoded", false);
	}
}

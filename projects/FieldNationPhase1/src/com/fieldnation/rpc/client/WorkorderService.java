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
		return httpGet(resultCode, "/api/rest/v1/workorder/requested",
				"?page=" + page, allowCache);
	}

	public Intent getAvailable(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/available",
				"?page=" + page, allowCache);
	}

	public Intent getPendingApproval(int resultCode, int page,
			boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/pending_approval",
				"?page=" + page, allowCache);
	}

	public Intent getAssigned(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/assigned",
				"?page=" + page, allowCache);
	}

	public Intent getCompleted(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/completed",
				"?page=" + page, allowCache);
	}

	public Intent getCanceled(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/workorder/canceled",
				"?page=" + page, allowCache);
	}

	public Intent getDetails(int resultCode, long workorderId,
			boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/details",
				allowCache);
	}

	public Intent decline(int resultCode, long workorderId, boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/decline", null, "",
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent addRequest(int resultCode, long workorderId,
			int expireInSeconds, boolean allowCache) {
		if (expireInSeconds == -1)
			return httpPost(resultCode,
					"/api/rest/v1/workorder/" + workorderId + "/request", null,
					"", "application/x-www-form-urlencoded", allowCache);

		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/request", null,
				"expiration=" + expireInSeconds,
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent removeRequest(int resultCode, long workorderId,
			boolean allowCache) {
		return httpDelete(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/request", null,
				allowCache);
	}

	public Intent confirmAssignment(int resultCode, long workorderId,
			long startTime, long endTime, boolean allowCache) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/assignment",
				null,
				"start_time=" + ISO8601.fromUTC(startTime) + "&end_time=" + ISO8601.fromUTC(endTime),
				"application/x-www-form-urlencoded", allowCache);
	}

	// TODO look up viable reasons and categories
	public Intent cancelAssignment(int resultCode, long workorderId,
			String cancelCategory, String cancelReason, boolean allowCache) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/cancel-assignment",
				null,
				"cancel_category=" + cancelCategory + "&cancel_reason=" + misc.escapeForURL(cancelReason),
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent ready(int resultCode, long workorderId,
			String cancelCategory, String cancelReason, boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/ready", null, "",
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent checkin(int resultCode, long workorderId, long checkinTime,
			boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkin_time=" + ISO8601.fromUTC(checkinTime),
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent checkout(int resultCode, long workorderId, long checkoutTime,
			boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkout_time=" + ISO8601.fromUTC(checkoutTime),
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent closingNotes(int resultCode, long workorderId, String notes,
			boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/closing-notes",
				null, "notes=" + misc.escapeForURL(notes),
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent getMessages(int resultCode, long workorderId,
			boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages",
				allowCache);
	}

	public Intent newMessage(int resultCode, long workorderId, String message,
			boolean allowCache) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages/new",
				null, "message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded", allowCache);
	}

	public Intent newMessage(int resultCode, long workorderId, long messageId,
			String message, boolean allowCache) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages/" + messageId + "/reply",
				null, "message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded", allowCache);
	}

}

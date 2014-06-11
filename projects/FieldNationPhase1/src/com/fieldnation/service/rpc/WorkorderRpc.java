package com.fieldnation.service.rpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import com.fieldnation.utils.misc;
import com.fieldnation.webapi.AccessToken;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class WorkorderRpc extends WebRpcCaller {

	public WorkorderRpc(Context conetxt, AccessToken at, ResultReceiver callback) {
		super(conetxt, at, callback);
	}

	public Intent getRequested(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/requested",
				"?page=" + page);
	}

	public Intent getAvailable(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/available",
				"?page=" + page);
	}

	public Intent getPendingApproval(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/pending_approval",
				"?page=" + page);
	}

	public Intent getAssigned(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/assigned",
				"?page=" + page);
	}

	public Intent getCompleted(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/completed",
				"?page=" + page);
	}

	public Intent getCanceled(int resultCode, int page) {
		return httpGet(resultCode, "/api/rest/v1/workorder/canceled",
				"?page=" + page);
	}

	public Intent getDetails(int resultCode, long workorderId) {
		return httpGet(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/details");
	}

	public Intent decline(int resultCode, long workorderId) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/decline", null, "",
				"application/x-www-form-urlencoded");
	}

	public Intent addRequest(int resultCode, long workorderId,
			int expireInSeconds) {
		if (expireInSeconds == -1)
			return httpPost(resultCode,
					"/api/rest/v1/workorder/" + workorderId + "/request", null,
					"", "application/x-www-form-urlencoded");

		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/request", null,
				"expiration=" + expireInSeconds,
				"application/x-www-form-urlencoded");
	}

	public Intent removeRequest(int resultCode, long workorderId) {
		return httpDelete(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/request", null);
	}

	public Intent confirmAssignment(int resultCode, long workorderId,
			long startTime, long endTime) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/assignment",
				null,
				"start_time=" + misc.utcTo8601(startTime) + "&end_time=" + misc.utcTo8601(endTime),
				"application/x-www-form-urlencoded");
	}

	// TODO look up viable reasons and categories
	public Intent cancelAssignment(int resultCode, long workorderId,
			String cancelCategory, String cancelReason) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/cancel-assignment",
				null,
				"cancel_category=" + cancelCategory + "&cancel_reason=" + cancelReason,
				"application/x-www-form-urlencoded");
	}

	public Intent ready(int resultCode, long workorderId,
			String cancelCategory, String cancelReason) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/ready", null, "",
				"application/x-www-form-urlencoded");
	}

	public Intent checkin(int resultCode, long workorderId, long checkinTime) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkin_time=" + misc.utcTo8601(checkinTime),
				"application/x-www-form-urlencoded");
	}

	public Intent checkout(int resultCode, long workorderId, long checkoutTime) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/checkin", null,
				"checkout_time=" + misc.utcTo8601(checkoutTime),
				"application/x-www-form-urlencoded");
	}

	public Intent closingNotes(int resultCode, long workorderId, String notes) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/closing-notes",
				null, "notes=" + misc.escapeForURL(notes),
				"application/x-www-form-urlencoded");
	}

	public Intent getMessages(int resultCode, long workorderId) {
		return httpGet(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages");
	}

	public Intent newMessage(int resultCode, long workorderId, String message) {
		return httpPost(resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages/new",
				null, "message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded");
	}

	public Intent newMessage(int resultCode, long workorderId, long messageId,
			String message) {
		return httpPost(
				resultCode,
				"/api/rest/v1/workorder/" + workorderId + "/messages/" + messageId + "/reply",
				null, "message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded");
	}

}

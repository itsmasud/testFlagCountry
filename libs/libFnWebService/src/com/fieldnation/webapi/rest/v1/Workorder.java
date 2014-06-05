package com.fieldnation.webapi.rest.v1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

public class Workorder extends Ws {

	public Workorder(AccessToken token) {
		super(token);
	}

	/**
	 * 
	 * @param page
	 *            sets the page number to get, set to leave out
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Result getRequested(int page) throws IOException, ParseException {
		if (page == -1)
			return httpGet("/api/rest/v1/workorder/requested");
		else
			return httpGet("/api/rest/v1/workorder/requested", "?page=" + page,
					null);
	}

	public Result getAvailable(int page) throws MalformedURLException,
			ParseException, IOException {
		if (page == -1)
			return httpGet("/api/rest/v1/workorder/available");
		else
			return httpGet("/api/rest/v1/workorder/available", "?page=" + page,
					null);
	}

	public Result getPendingApproval(int page) throws MalformedURLException,
			ParseException, IOException {
		if (page == -1)
			return httpGet("/api/rest/v1/workorder/pending-approval");
		else
			return httpGet("/api/rest/v1/workorder/pending_approval", "?page="
					+ page, null);
	}

	public Result getAssigned(int page) throws MalformedURLException,
			ParseException, IOException {
		if (page == -1)
			return httpGet("/api/rest/v1/workorder/assigned");
		else
			return httpGet("/api/rest/v1/workorder/assigned", "?page=" + page,
					null);
	}

	public Result getDetails(long workorderId) throws MalformedURLException,
			IOException, ParseException {
		return httpGet("/api/rest/v1/workorder/" + workorderId + "/details");
	}

	public Result decline(long workorderId) throws MalformedURLException,
			IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId + "/decline",
				null, "", "application/x-www-form-urlencoded");
	}

	public Result addRequest(long workorderId, int expireInSeconds)
			throws MalformedURLException, IOException, ParseException {
		if (expireInSeconds == -1)
			return httpPost("/api/rest/v1/workorder/" + workorderId
					+ "/request", null, "", "application/x-www-form-urlencoded");

		return httpPost("/api/rest/v1/workorder/" + workorderId + "/request",
				null, "expiration=" + expireInSeconds,
				"application/x-www-form-urlencoded");
	}

	public Result removeRequest(long workorderId) throws MalformedURLException,
			IOException, ParseException {
		return httpDelete("/api/rest/v1/workorder/" + workorderId + "/request",
				null, null);
	}

	public Result confirmAssignment(long workorderId, long startTime,
			long endTime) throws MalformedURLException, IOException,
			ParseException {
		return httpPost(
				"/api/rest/v1/workorder/" + workorderId + "/assignment",
				null,
				"start_time=" + misc.utcTo8601(startTime) + "&end_time="
						+ misc.utcTo8601(endTime),
				"application/x-www-form-urlencoded");
	}

	// TODO look up viable reasons and categories
	public Result cancelAssignment(long workorderId, String cancelCategory,
			String cancelReason) throws MalformedURLException, IOException,
			ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId
				+ "/cancel-assignment", null, "cancel_category="
				+ cancelCategory + "&cancel_reason=" + cancelReason,
				"application/x-www-form-urlencoded");
	}

	public Result ready(long workorderId, String cancelCategory,
			String cancelReason) throws MalformedURLException, IOException,
			ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId + "/ready",
				null, "", "application/x-www-form-urlencoded");
	}

	public Result checkin(long workorderId, long checkinTime)
			throws MalformedURLException, IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId + "/checkin",
				null, "checkin_time=" + misc.utcTo8601(checkinTime),
				"application/x-www-form-urlencoded");
	}

	public Result checkout(long workorderId, long checkoutTime)
			throws MalformedURLException, IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId + "/checkin",
				null, "checkout_time=" + misc.utcTo8601(checkoutTime),
				"application/x-www-form-urlencoded");
	}

	public Result closingNotes(long workorderId, String notes)
			throws MalformedURLException, IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId
				+ "/closing-notes", null, "notes=" + misc.escapeForURL(notes),
				"application/x-www-form-urlencoded");
	}

	public Result getMessages(long workorderId) throws MalformedURLException,
			IOException, ParseException {
		return httpGet("/api/rest/v1/workorder/" + workorderId + "/messages");
	}

	public Result newMessage(long workorderId, String message)
			throws MalformedURLException, IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId
				+ "/messages/new", null,
				"message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded");
	}

	public Result newMessage(long workorderId, long messageId, String message)
			throws MalformedURLException, IOException, ParseException {
		return httpPost("/api/rest/v1/workorder/" + workorderId + "/messages/"
				+ messageId + "/reply", null,
				"message=" + misc.escapeForURL(message),
				"application/x-www-form-urlencoded");
	}
}

package com.fieldnation.data.messages;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Replies {
	@Json(name = "messageId")
	private Integer _messageId;
	@Json(name = "date")
	private String _date;
	@Json(name = "workorderId")
	private Integer _workorderId;
	@Json(name = "message")
	private String _message;
	@Json(name = "messageFrom")
	private String _messageFrom;

	public Replies() {
	}

	public Integer getMessageId() {
		return _messageId;
	}

	public String getDate() {
		return _date;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public String getMessage() {
		return _message;
	}

	public String getMessageFrom() {
		return _messageFrom;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Replies replies) {
		try {
			return Serializer.serializeObject(replies);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Replies fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Replies.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

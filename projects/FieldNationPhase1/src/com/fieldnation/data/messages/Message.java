package com.fieldnation.data.messages;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name = "messageId")
	private Long _messageId;
	@Json(name = "date")
	private String _date;
	@Json(name = "workorderId")
	private Integer _workorderId;
	@Json(name = "replies")
	private Replies[] _replies;
	@Json(name = "message")
	private String _message;
	@Json(name = "messageFrom")
	private String _messageFrom;

	public Message() {
	}

	public Long getMessageId() {
		return _messageId;
	}

	public String getDate() {
		return _date;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public Replies[] getReplies() {
		return _replies;
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

	public static JsonObject toJson(Message message) {
		try {
			return Serializer.serializeObject(message);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Message fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Message.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

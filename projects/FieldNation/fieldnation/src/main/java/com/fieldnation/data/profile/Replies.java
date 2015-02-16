package com.fieldnation.data.profile;

import com.fieldnation.data.workorder.User;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Replies {
	@Json(name = "date")
	private String _date;
	@Json(name="file_url")
	private String _fileUrl;
	@Json(name="fromUser")
	private User _fromUser;
	@Json(name="fromUserId")
	private Integer _fromUserId;
	@Json(name = "message")
	private String _message;
	@Json(name = "messageFrom")
	private String _messageFrom;
	@Json(name="messageId")
	private Integer _messageId;
	@Json(name="workorderId")
	private Integer _workorderId;

	public Replies() {
	}
	public String getDate(){
		return _date;
	}

	public String getFileUrl(){
		return _fileUrl;
	}

	public User getFromUser(){
		return _fromUser;
	}

	public Integer getFromUserId(){
		return _fromUserId;
	}

	public String getMessage() {
		return _message;
	}

	public String getMessageFrom() {
		return _messageFrom;
	}

	public Integer getMessageId(){
		return _messageId;
	}

	public Integer getWorkorderId(){
		return _workorderId;
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

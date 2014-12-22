package com.fieldnation.data.profile;

import com.fieldnation.data.workorder.Status;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name = "date")
	private String _date;
	@Json(name = "fileUrl")
	private String _fileUrl;
	@Json(name = "file_url")
	private String _file_Url;
	@Json(name = "message")
	private String _message;
	@Json(name = "messageFrom")
	private String _messageFrom;
	@Json(name = "messageId")
	private Integer _messageId;
	@Json(name = "photoThumbUrl")
	private String _photoThumbUrl;
	@Json(name = "photoUrl")
	private String _photoUrl;
	@Json(name = "replies")
	private Replies[] _replies;
	@Json(name = "status")
	private Status _status;
	@Json(name = "workorderId")
	private Long _workorderId;
	@Json(name = "workorderTitle")
	private String _workorderTitle;

	public Message() {
	}

	public String getDate() {
		return _date;
	}

	public String getFileUrl() {
		if (_fileUrl == null)
			return _file_Url;
		return _fileUrl;
	}

	public String getMessage() {
		return _message;
	}

	public String getMessageFrom() {
		return _messageFrom;
	}

	public Integer getMessageId() {
		return _messageId;
	}

	public String getPhotoThumbUrl() {
		return _photoThumbUrl;
	}

	public String getPhotoUrl() {
		return _photoUrl;
	}

	public Replies[] getReplies() {
		return _replies;
	}

	public Status getStatus() {
		return _status;
	}

	public Long getWorkorderId() {
		return _workorderId;
	}

	public String getWorkorderTitle() {
		return _workorderTitle;
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

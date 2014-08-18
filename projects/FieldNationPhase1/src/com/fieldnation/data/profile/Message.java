package com.fieldnation.data.profile;

import com.fieldnation.data.workorder.Status;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name="workorderTitle")
	private String _workorderTitle;
	@Json(name = "file_url")
	private String _file_Url;
	@Json(name = "date")
	private String _date;
	@Json(name="status")
	private Status _status;
	@Json(name = "photoUrl")
	private String _photoUrl;
	@Json(name = "photoThumbUrl")
	private String _photoThumbUrl;
	@Json(name = "replies")
	private Replies[] _replies;
	@Json(name = "messageFrom")
	private String _messageFrom;
	@Json(name = "messageId")
	private Long _messageId;
	@Json(name = "workorderId")
	private Integer _workorderId;
	@Json(name = "fileUrl")
	private String _fileUrl;
	@Json(name = "message")
	private String _message;

	public Message() {
	}

	public String getFileUrl() {
		if (_fileUrl == null)
			return _file_Url;
		return _fileUrl;
	}

	public String getDate() {
		return _date;
	}

	public Status getStatus(){
		return _status;
	}

	public String getPhotoUrl() {
		return _photoUrl;
	}

	public String getPhotoThumbUrl() {
		return _photoThumbUrl;
	}

	public Replies[] getReplies() {
		return _replies;
	}

	public String getMessageFrom() {
		return _messageFrom;
	}

	public Long getMessageId() {
		return _messageId;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public String getMessage() {
		return _message;
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

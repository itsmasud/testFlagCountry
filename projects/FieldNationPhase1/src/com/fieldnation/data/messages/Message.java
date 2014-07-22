package com.fieldnation.data.messages;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name="photo_url")
	private String _photoUrl;
	@Json(name = "date")
	private String _date;
	@Json(name="file_url")
	private String _fileUrl;
	@Json(name = "replies")
	private Replies[] _replies;
	@Json(name = "messageFrom")
	private String _messageFrom;
	@Json(name="messageId")
	private Integer _messageId;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name="photo_thumb_url")
	private String _photoThumbUrl;
	@Json(name="message")
	private String _message;

	public Message() {
	}
	public String getPhotoUrl(){
		return _photoUrl;
	}

	public String getDate() {
		return _date;
	}

	public String getFileUrl(){
		return _fileUrl;
	}

	public Replies[] getReplies() {
		return _replies;
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

	public String getPhotoThumbUrl(){
		return _photoThumbUrl;
	}

	public String getMessage(){
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

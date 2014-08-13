package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name="msgReadDate")
	private String _msgReadDate;
	@Json(name="msgId")
	private Integer _msgId;
	@Json(name="threadId")
	private Double _threadId;
	@Json(name="parentId")
	private Integer _parentId;
	@Json(name="toUser")
	private User _toUser;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name="fromUser")
	private User _fromUser;
	@Json(name="msgCreateDate")
	private String _msgCreateDate;
	@Json(name="msgRead")
	private Integer _msgRead;
	@Json(name="msgPermission")
	private Integer _msgPermission;
	@Json(name="message")
	private String _message;

	public Message() {
	}
	public String getMsgReadDate(){
		return _msgReadDate;
	}

	public Integer getMsgId(){
		return _msgId;
	}

	public Double getThreadId(){
		return _threadId;
	}

	public Integer getParentId(){
		return _parentId;
	}

	public User getToUser(){
		return _toUser;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public User getFromUser(){
		return _fromUser;
	}

	public String getMsgCreateDate(){
		return _msgCreateDate;
	}

	public Integer getMsgRead(){
		return _msgRead;
	}

	public Integer getMsgPermission(){
		return _msgPermission;
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

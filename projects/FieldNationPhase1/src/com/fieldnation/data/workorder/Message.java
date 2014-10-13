package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name="fromUser")
	private User _fromUser;
	@Json(name="message")
	private String _message;
	@Json(name="msgCreateDate")
	private String _msgCreateDate;
	@Json(name="msgId")
	private Integer _msgId;
	@Json(name="msgPermission")
	private Integer _msgPermission;
	@Json(name="msgRead")
	private Integer _msgRead;
	@Json(name="msgReadDate")
	private String _msgReadDate;
	@Json(name="parentId")
	private Integer _parentId;
	@Json(name="threadId")
	private Double _threadId;
	@Json(name="toUser")
	private User _toUser;
	@Json(name="workorderId")
	private Integer _workorderId;

	public Message() {
	}
	public User getFromUser(){
		return _fromUser;
	}

	public String getMessage(){
		return _message;
	}

	public String getMsgCreateDate(){
		return _msgCreateDate;
	}

	public Integer getMsgId(){
		return _msgId;
	}

	public Integer getMsgPermission(){
		return _msgPermission;
	}

	public Integer getMsgRead(){
		return _msgRead;
	}

	public String getMsgReadDate(){
		return _msgReadDate;
	}

	public Integer getParentId(){
		return _parentId;
	}

	public Double getThreadId(){
		return _threadId;
	}

	public User getToUser(){
		return _toUser;
	}

	public Integer getWorkorderId(){
		return _workorderId;
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

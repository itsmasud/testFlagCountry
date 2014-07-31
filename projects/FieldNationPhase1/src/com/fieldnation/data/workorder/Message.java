package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message {
	@Json(name="emailMessageId")
	private String _emailMessageId;
	@Json(name="msgId")
	private Integer _msgId;
	@Json(name="msgFrom")
	private Integer _msgFrom;
	@Json(name="msgRead")
	private Integer _msgRead;
	@Json(name="fromGroupName")
	private String _fromGroupName;
	@Json(name="fromUserName")
	private String _fromUserName;
	@Json(name="toCompanyName")
	private String _toCompanyName;
	@Json(name="fromCompanyId")
	private Integer _fromCompanyId;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name = "message")
	private String _message;
	@Json(name="msgCreateDate")
	private String _msgCreateDate;
	@Json(name="threadId")
	private Double _threadId;
	@Json(name="msgPermission")
	private Integer _msgPermission;
	@Json(name="msgTo")
	private Integer _msgTo;
	@Json(name="msgReadDate")
	private String _msgReadDate;
	@Json(name="fromCompanyName")
	private String _fromCompanyName;
	@Json(name="toGroupName")
	private String _toGroupName;
	@Json(name="zendeskTicketId")
	private Integer _zendeskTicketId;
	@Json(name="parentId")
	private Integer _parentId;
	@Json(name="toUserName")
	private String _toUserName;
	@Json(name="toCompanyId")
	private Integer _toCompanyId;
	@Json(name="msgReadId")
	private Integer _msgReadId;
	@Json(name="readBy")
	private Integer _readBy;

	public Message() {
	}
	public String getEmailMessageId(){
		return _emailMessageId;
	}

	public Integer getMsgId(){
		return _msgId;
	}

	public Integer getMsgFrom(){
		return _msgFrom;
	}

	public Integer getMsgRead(){
		return _msgRead;
	}

	public String getFromGroupName(){
		return _fromGroupName;
	}

	public String getFromUserName(){
		return _fromUserName;
	}

	public String getToCompanyName(){
		return _toCompanyName;
	}

	public Integer getFromCompanyId() {
		return _fromCompanyId;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public String getMessage() {
		return _message;
	}

	public String getMsgCreateDate(){
		return _msgCreateDate;
	}

	public Double getThreadId(){
		return _threadId;
	}

	public Integer getMsgPermission(){
		return _msgPermission;
	}

	public Integer getMsgTo(){
		return _msgTo;
	}

	public String getMsgReadDate(){
		return _msgReadDate;
	}

	public String getFromCompanyName(){
		return _fromCompanyName;
	}

	public String getToGroupName(){
		return _toGroupName;
	}

	public Integer getZendeskTicketId(){
		return _zendeskTicketId;
	}

	public Integer getParentId(){
		return _parentId;
	}

	public String getToUserName() {
		return _toUserName;
	}

	public Integer getToCompanyId(){
		return _toCompanyId;
	}

	public Integer getMsgReadId(){
		return _msgReadId;
	}

	public Integer getReadBy(){
		return _readBy;
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

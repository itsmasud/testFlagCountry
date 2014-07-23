package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Message{
	@Json(name="workorder_id")
	private Integer _workorderId;
	@Json(name="from_user_name")
	private String _fromUserName;
	@Json(name="email_message_id")
	private String _emailMessageId;
	@Json(name="msg_read_id")
	private Integer _msgReadId;
	@Json(name="msg_to")
	private Integer _msgTo;
	@Json(name="msg_permission")
	private Integer _msgPermission;
	@Json(name="from_company_id")
	private Integer _fromCompanyId;
	@Json(name="msg_from")
	private Integer _msgFrom;
	@Json(name="message")
	private String _message;
	@Json(name="msg_read")
	private Integer _msgRead;
	@Json(name="msg_read_date")
	private String _msgReadDate;
	@Json(name="to_company_id")
	private Integer _toCompanyId;
	@Json(name="read_by")
	private Integer _readBy;
	@Json(name="thread_id")
	private Double _threadId;
	@Json(name="zendesk_ticket_id")
	private Integer _zendeskTicketId;
	@Json(name="from_group_name")
	private String _fromGroupName;
	@Json(name="parent_id")
	private Integer _parentId;
	@Json(name="to_group_name")
	private String _toGroupName;
	@Json(name="to_user_name")
	private String _toUserName;
	@Json(name="msg_id")
	private Integer _msgId;
	@Json(name="msg_create_date")
	private String _msgCreateDate;
	@Json(name="from_company_name")
	private String _fromCompanyName;
	@Json(name="to_company_name")
	private String _toCompanyName;

	public Message(){
	}
	public Integer getWorkorderId(){
		return _workorderId;
	}

	public String getFromUserName(){
		return _fromUserName;
	}

	public String getEmailMessageId(){
		return _emailMessageId;
	}

	public Integer getMsgReadId(){
		return _msgReadId;
	}

	public Integer getMsgTo(){
		return _msgTo;
	}

	public Integer getMsgPermission(){
		return _msgPermission;
	}

	public Integer getFromCompanyId(){
		return _fromCompanyId;
	}

	public Integer getMsgFrom(){
		return _msgFrom;
	}

	public String getMessage(){
		return _message;
	}

	public Integer getMsgRead(){
		return _msgRead;
	}

	public String getMsgReadDate(){
		return _msgReadDate;
	}

	public Integer getToCompanyId(){
		return _toCompanyId;
	}

	public Integer getReadBy(){
		return _readBy;
	}

	public Double getThreadId(){
		return _threadId;
	}

	public Integer getZendeskTicketId(){
		return _zendeskTicketId;
	}

	public String getFromGroupName(){
		return _fromGroupName;
	}

	public Integer getParentId(){
		return _parentId;
	}

	public String getToGroupName(){
		return _toGroupName;
	}

	public String getToUserName(){
		return _toUserName;
	}

	public Integer getMsgId(){
		return _msgId;
	}

	public String getMsgCreateDate(){
		return _msgCreateDate;
	}

	public String getFromCompanyName(){
		return _fromCompanyName;
	}

	public String getToCompanyName(){
		return _toCompanyName;
	}

	public JsonObject toJson(){
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

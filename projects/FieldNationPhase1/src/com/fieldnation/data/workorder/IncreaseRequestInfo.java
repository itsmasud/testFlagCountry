package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class IncreaseRequestInfo{
	@Json(name="workorderIncreaseId")
	private Integer _workorderIncreaseId;
	@Json(name="flagId")
	private Integer _flagId;
	@Json(name="statusName")
	private String _statusName;
	@Json(name="status")
	private Integer _status;
	@Json(name="denyReason")
	private Object _denyReason;
	@Json(name="techUserId")
	private Integer _techUserId;
	@Json(name="payRateDiff")
	private Integer _payRateDiff;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name="payTermDescription")
	private String _payTermDescription;
	@Json(name="payTermId")
	private Integer _payTermId;
	@Json(name="createdTime")
	private String _createdTime;
	@Json(name="requestReason")
	private String _requestReason;
	@Json(name="techFullName")
	private String _techFullName;

	public IncreaseRequestInfo(){
	}
	public Integer getWorkorderIncreaseId(){
		return _workorderIncreaseId;
	}

	public Integer getFlagId(){
		return _flagId;
	}

	public String getStatusName(){
		return _statusName;
	}

	public Integer getStatus(){
		return _status;
	}

	public Object getDenyReason(){
		return _denyReason;
	}

	public Integer getTechUserId(){
		return _techUserId;
	}

	public Integer getPayRateDiff(){
		return _payRateDiff;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public String getPayTermDescription(){
		return _payTermDescription;
	}

	public Integer getPayTermId(){
		return _payTermId;
	}

	public String getCreatedTime(){
		return _createdTime;
	}

	public String getRequestReason(){
		return _requestReason;
	}

	public String getTechFullName(){
		return _techFullName;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(IncreaseRequestInfo increaseRequestInfo) {
		try {
			return Serializer.serializeObject(increaseRequestInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static IncreaseRequestInfo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(IncreaseRequestInfo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

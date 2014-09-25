package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Task {
	@Json(name = "showTimeMenu")
	private Boolean _showTimeMenu;
	@Json(name = "timeRelativeTo")
	private String _timeRelativeTo;
	@Json(name = "alertOnCompletionInput")
	private String _alertOnCompletionInput;
	@Json(name = "alertOnCompletion")
	private String[] _alertOnCompletion;
	@Json(name="taskIsCompleted")
	private Integer _taskIsCompleted;
	@Json(name = "taskId")
	private Integer _taskId;
	@Json(name = "identifier")
	private Integer _identifier;
	@Json(name = "type")
	private String _type;
	@Json(name = "emailAddress")
	private String _emailAddress;
	@Json(name = "typeId")
	private Integer _typeId;
	@Json(name = "assignedTo")
	private String _assignedTo;
	@Json(name = "showAlertMenu")
	private Boolean _showAlertMenu;
	@Json(name="taskIdentifier")
	private Integer _taskIdentifier;
	@Json(name = "phoneNumber")
	private String _phoneNumber;
	@Json(name = "completedAtDate")
	private String _completedAtDate;
	@Json(name="workorderTaskId")
	private Integer _workorderTaskId;
	@Json(name = "isEditing")
	private Boolean _isEditing;
	@Json(name = "minutes")
	private Integer _minutes;
	@Json(name="customField")
	private Integer _customField;
	@Json(name = "canDelete")
	private Boolean _canDelete;
	@Json(name = "description")
	private String _description;
	@Json(name = "order")
	private Integer _order;
	@Json(name = "hours")
	private Integer _hours;
	@Json(name = "stage")
	private String _stage;
	@Json(name = "completed")
	private Boolean _completed;
	@Json(name = "isNew")
	private Boolean _isNew;

	public Task() {
	}

	public Boolean getShowTimeMenu() {
		return _showTimeMenu;
	}

	public String getTimeRelativeTo() {
		return _timeRelativeTo;
	}

	public String getAlertOnCompletionInput() {
		return _alertOnCompletionInput;
	}

	public String[] getAlertOnCompletion() {
		return _alertOnCompletion;
	}

	public Integer getTaskIsCompleted(){
		return _taskIsCompleted;
	}

	public Integer getTaskId() {
		return _taskId;
	}

	public Integer getIdentifier() {
		return _identifier;
	}

	public String getType() {
		return _type;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public Integer getTypeId() {
		return _typeId;
	}

	public String getAssignedTo() {
		return _assignedTo;
	}

	public Boolean getShowAlertMenu() {
		return _showAlertMenu;
	}

	public Integer getTaskIdentifier(){
		return _taskIdentifier;
	}

	public Object getPhoneNumber(){
		return _phoneNumber;
	}

	public String getCompletedAtDate() {
		return _completedAtDate;
	}

	public Integer getWorkorderTaskId(){
		return _workorderTaskId;
	}

	public Boolean getIsEditing(){
		return _isEditing;
	}

	public Integer getMinutes() {
		return _minutes;
	}

	public Integer getCustomField(){
		return _customField;
	}

	public Boolean getCanDelete() {
		return _canDelete;
	}

	public String getDescription() {
		return _description;
	}

	public Integer getOrder() {
		return _order;
	}

	public Integer getHours() {
		return _hours;
	}

	public String getStage() {
		return _stage;
	}

	public Boolean getCompleted() {
		return _completed;
	}

	public Boolean getIsNew() {
		return _isNew;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Task task) {
		try {
			return Serializer.serializeObject(task);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Task fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Task.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public TaskType getTaskType() {
		return TaskType.fromId(_typeId);
	}

}

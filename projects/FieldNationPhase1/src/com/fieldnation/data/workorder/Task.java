package com.fieldnation.data.workorder;

import java.text.ParseException;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Task implements Parcelable {

	@Json(name = "alertOnCompletion")
	private String[] _alertOnCompletion;
	@Json(name="alertOnCompletionInput")
	private String _alertOnCompletionInput;
	@Json(name = "assignedTo")
	private String _assignedTo;
	@Json(name="canDelete")
	private Boolean _canDelete;
	@Json(name="completed")
	private Boolean _completed;
	@Json(name = "completedAtDate")
	private String _completedAtDate;
	@Json(name = "customField")
	private Integer _customField;
	@Json(name = "description")
	private String _description;
	@Json(name="emailAddress")
	private String _emailAddress;
	@Json(name = "hours")
	private Integer _hours;
	@Json(name="identifier")
	private Integer _identifier;
	@Json(name="isEditing")
	private Boolean _isEditing;
	@Json(name = "isNew")
	private Boolean _isNew;
	@Json(name="minutes")
	private Integer _minutes;
	@Json(name="order")
	private Integer _order;
	@Json(name="phoneNumber")
	private Double _phoneNumber;
	@Json(name="showAlertMenu")
	private Boolean _showAlertMenu;
	@Json(name="showTimeMenu")
	private Boolean _showTimeMenu;
	@Json(name="stage")
	private String _stage;
	@Json(name="taskId")
	private Integer _taskId;
	@Json(name="taskIdentifier")
	private Integer _taskIdentifier;
	@Json(name="taskIsCompleted")
	private Integer _taskIsCompleted;
	@Json(name="timeRelativeTo")
	private String _timeRelativeTo;
	@Json(name="type")
	private String _type;
	@Json(name="typeId")
	private Integer _typeId;
	@Json(name="workorderTaskId")
	private Integer _workorderTaskId;

	public Task() {
	}
	public String[] getAlertOnCompletion(){
		return _alertOnCompletion;
	}

	public String getAlertOnCompletionInput(){
		return _alertOnCompletionInput;
	}

	public String getAssignedTo(){
		return _assignedTo;
	}

	public Boolean getCanDelete(){
		return _canDelete;
	}

	public Boolean getCompleted(){
		return _completed;
	}

	public String getCompletedAtDate(){
		return _completedAtDate;
	}

	public Integer getCustomField(){
		return _customField;
	}

	public String getDescription(){
		return _description;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public Integer getHours(){
		return _hours;
	}

	public Integer getIdentifier(){
		return _identifier;
	}

	public Boolean getIsEditing(){
		return _isEditing;
	}

	public Boolean getIsNew(){
		return _isNew;
	}

	public Integer getMinutes(){
		return _minutes;
	}

	public Integer getOrder(){
		return _order;
	}

	public Double getPhoneNumber(){
		return _phoneNumber;
	}

	public Boolean getShowAlertMenu(){
		return _showAlertMenu;
	}

	public Boolean getShowTimeMenu(){
		return _showTimeMenu;
	}

	public String getStage(){
		return _stage;
	}

	public Integer getTaskId(){
		return _taskId;
	}

	public Integer getTaskIdentifier(){
		return _taskIdentifier;
	}

	public Integer getTaskIsCompleted(){
		return _taskIsCompleted;
	}

	public String getTimeRelativeTo(){
		return _timeRelativeTo;
	}

	public String getType(){
		return _type;
	}

	public Integer getTypeId(){
		return _typeId;
	}

	public Integer getWorkorderTaskId(){
		return _workorderTaskId;
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


	/*-*********************************************-*/
	/*-				Not Generated Code				-*/
	/*-*********************************************-*/
	public TaskType getTaskType() {
		return TaskType.fromId(_typeId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(toJson().toString());
	}

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

		@Override
		public Task createFromParcel(Parcel source) {
			try {
				return Task.fromJson(new JsonObject(source.readString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

}

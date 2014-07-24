package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Task {
	@Json(name="task_id")
	private Integer _taskId;
	@Json(name="type")
	private String _type;
	@Json(name="completed")
	private Boolean _completed;
	@Json(name="dateCompleted")
	private String _dateCompleted;

	public Task() {
	}
	public Integer getTaskId() {
		return _taskId;
	}

	public String getType(){
		return _type;
	}

	public Boolean getCompleted(){
		return _completed;
	}

	public String getDateCompleted(){
		return _dateCompleted;
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

}

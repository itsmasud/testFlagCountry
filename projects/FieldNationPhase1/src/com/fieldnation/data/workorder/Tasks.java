package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Tasks {
	@Json(name = "task_id")
	private Integer _taskId;
	@Json(name = "completed")
	private Boolean _completed;
	@Json(name = "type")
	private String _type;
	@Json(name = "dateCompleted")
	private String _dateCompleted;

	public Tasks() {
	}

	public Integer getTaskId() {
		return _taskId;
	}

	public Boolean getCompleted() {
		return _completed;
	}

	public String getType() {
		return _type;
	}

	public String getDateCompleted() {
		return _dateCompleted;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Tasks tasks) {
		try {
			return Serializer.serializeObject(tasks);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Tasks fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Tasks.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

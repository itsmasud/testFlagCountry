package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Schedule {
	// @Json(name="endTime")
	// private String _endTime;
	@Json(name = "end_time")
	private String _endTime;
	// @Json(name="startTime")
	// private String _startTime;
	@Json(name = "start_time")
	private String _startTime;

	public Schedule() {
	}

	public String getEndTime() {
		return _endTime;
	}

	// public String getEndTime(){
	// return _endTime;
	// }

	public String getStartTime() {
		return _startTime;
	}

	// public String getStartTime(){
	// return _startTime;
	// }

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Schedule schedule) {
		try {
			return Serializer.serializeObject(schedule);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Schedule fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Schedule.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

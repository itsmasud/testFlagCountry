package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class LoggedWork{
	@Json(name="startDate")
	private String _startDate;
	@Json(name="hours")
	private Double _hours;
	@Json(name="endDate")
	private String _endDate;

	public LoggedWork(){
	}
	public String getStartDate(){
		return _startDate;
	}

	public Double getHours(){
		return _hours;
	}

	public String getEndDate(){
		return _endDate;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(LoggedWork loggedWork) {
		try {
			return Serializer.serializeObject(loggedWork);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static LoggedWork fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(LoggedWork.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

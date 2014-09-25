package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ScheduleRange {
	@Json(name = "startDate")
	private String _startDate;
	@Json(name = "endDate")
	private String _endDate;

	public ScheduleRange() {
	}

	public String getStartDate() {
		return _startDate;
	}

	public String getEndDate() {
		return _endDate;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(ScheduleRange scheduleRange) {
		try {
			return Serializer.serializeObject(scheduleRange);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ScheduleRange fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ScheduleRange.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

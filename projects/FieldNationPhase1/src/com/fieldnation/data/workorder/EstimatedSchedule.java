package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class EstimatedSchedule {
	@Json(name="workorderScheduleId")
	private Integer _workorderScheduleId;
	@Json(name = "endTime")
	private String _endTime;
	@Json(name = "startTime")
	private String _startTime;
	@Json(name="duration")
	private Double _duration;

	public EstimatedSchedule() {
	}
	public Integer getWorkorderScheduleId(){
		return _workorderScheduleId;
	}

	public String getEndTime() {
		return _endTime;
	}

	public String getStartTime() {
		return _startTime;
	}

	public Double getDuration(){
		return _duration;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(EstimatedSchedule estimatedSchedule) {
		try {
			return Serializer.serializeObject(estimatedSchedule);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static EstimatedSchedule fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(EstimatedSchedule.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

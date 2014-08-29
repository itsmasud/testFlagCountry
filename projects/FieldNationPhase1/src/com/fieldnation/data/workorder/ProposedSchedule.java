package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ProposedSchedule{
	@Json(name="endTime")
	private Object _endTime;
	@Json(name="startTime")
	private String _startTime;
	@Json(name="startTimeStamp")
	private Integer _startTimeStamp;
	@Json(name="endTimeStamp")
	private Integer _endTimeStamp;

	public ProposedSchedule(){
	}
	public Object getEndTime(){
		return _endTime;
	}

	public String getStartTime(){
		return _startTime;
	}

	public Integer getStartTimeStamp(){
		return _startTimeStamp;
	}

	public Integer getEndTimeStamp(){
		return _endTimeStamp;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(ProposedSchedule proposedSchedule) {
		try {
			return Serializer.serializeObject(proposedSchedule);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ProposedSchedule fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ProposedSchedule.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

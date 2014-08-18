package com.fieldnation.data.accounting;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name="fullWorkDescription")
	private String _fullWorkDescription;
	@Json(name="endTime")
	private String _endTime;
	@Json(name = "amount")
	private Double _amount;
	@Json(name="workorderId")
	private long _workorderId;
	@Json(name="clientName")
	private String _clientName;
	@Json(name="startTime")
	private String _startTime;
	@Json(name="title")
	private String _title;

	public Workorder() {
	}
	public String getFullWorkDescription(){
		return _fullWorkDescription;
	}

	public String getEndTime(){
		return _endTime;
	}

	public Double getAmount(){
		return _amount;
	}

	public long getWorkorderId(){
		return _workorderId;
	}

	public String getClientName(){
		return _clientName;
	}

	public String getStartTime(){
		return _startTime;
	}

	public String getTitle(){
		return _title;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder workorder) {
		try {
			return Serializer.serializeObject(workorder);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

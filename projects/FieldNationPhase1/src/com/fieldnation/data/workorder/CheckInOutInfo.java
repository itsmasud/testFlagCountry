package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CheckInOutInfo{
	@Json(name="checkInId")
	private Integer _checkInId;
	@Json(name="checkOutId")
	private Integer _checkOutId;
	@Json(name="checkInTime")
	private String _checkInTime;
	@Json(name="totalHours")
	private Double _totalHours;
	@Json(name="checkOutTime")
	private String _checkOutTime;
	@Json(name="checkInDistance")
	private Double _checkInDistance;

	public CheckInOutInfo(){
	}
	public Integer getCheckInId(){
		return _checkInId;
	}

	public Integer getCheckOutId(){
		return _checkOutId;
	}

	public String getCheckInTime(){
		return _checkInTime;
	}

	public Double getTotalHours(){
		return _totalHours;
	}

	public String getCheckOutTime(){
		return _checkOutTime;
	}

	public Double getCheckInDistance(){
		return _checkInDistance;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(CheckInOutInfo checkInOutInfo) {
		try {
			return Serializer.serializeObject(checkInOutInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CheckInOutInfo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CheckInOutInfo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

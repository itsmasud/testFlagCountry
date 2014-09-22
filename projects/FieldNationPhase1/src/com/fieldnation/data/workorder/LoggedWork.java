package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class LoggedWork{
	@Json(name="checkoutLat")
	private Double _checkoutLat;
	@Json(name="checkoutLng")
	private Double _checkoutLng;
	@Json(name="startTime")
	private String _startTime;
	@Json(name="startDate")
	private String _startDate;
	@Json(name="noOfDevices")
	private Integer _noOfDevices;
	@Json(name="endDate")
	private String _endDate;
	@Json(name="endTime")
	private String _endTime;
	@Json(name="hoursType")
	private Integer _hoursType;
	@Json(name="loggedHoursId")
	private Integer _loggedHoursId;
	@Json(name="hours")
	private Double _hours;
	@Json(name="checkinLat")
	private Double _checkinLat;
	@Json(name="checkinLng")
	private Double _checkinLng;

	public LoggedWork(){
	}
	public Double getCheckoutLat(){
		return _checkoutLat;
	}

	public Double getCheckoutLng(){
		return _checkoutLng;
	}

	public String getStartTime(){
		return _startTime;
	}

	public String getStartDate(){
		return _startDate;
	}

	public Integer getNoOfDevices(){
		return _noOfDevices;
	}

	public String getEndDate(){
		return _endDate;
	}

	public String getEndTime(){
		return _endTime;
	}

	public Integer getHoursType(){
		return _hoursType;
	}

	public Integer getLoggedHoursId(){
		return _loggedHoursId;
	}

	public Double getHours(){
		return _hours;
	}

	public Double getCheckinLat(){
		return _checkinLat;
	}

	public Double getCheckinLng(){
		return _checkinLng;
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

package com.fieldnation.data.workorder;

import java.util.Calendar;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

public class Schedule {
	@Json(name="startTimeHours")
	private String _startTimeHours;
	@Json(name = "endTime")
	private String _endTime;
	@Json(name="endTimeHours")
	private String _endTimeHours;
	@Json(name = "startTime")
	private String _startTime;

	public Schedule() {
	}
	public String getStartTimeHours(){
		return _startTimeHours;
	}

	public String getEndTime() {
		return _endTime;
	}

	public String getEndTimeHours(){
		return _endTimeHours;
	}

	public String getStartTime() {
		return _startTime;
	}

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

	/*-*************************************************-*/
	/*-				Human Generated Code				-*/
	/*-*************************************************-*/

	public String getFormatedTime() {
		try {
			if (!misc.isEmptyOrNull(getStartTime())) {
				String when = "";
				Calendar cal = null;
				cal = ISO8601.toCalendar(getStartTime());
				when = misc.formatDate(cal);

				if (!misc.isEmptyOrNull(getEndTime())) {
					cal = ISO8601.toCalendar(getEndTime());
					if (cal.get(Calendar.YEAR) > 2000) {
						when += " - ";
						when += misc.formatDate(cal);
					}
				}
				when += " @ ";

				when += (cal.get(Calendar.HOUR) + 1) + (cal.get(Calendar.AM_PM) == Calendar.PM ? "pm" : "am");

				return when;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

}

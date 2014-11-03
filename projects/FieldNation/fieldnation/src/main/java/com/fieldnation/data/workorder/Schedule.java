package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.util.Calendar;

public class Schedule implements Parcelable {

    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "endTimeHours")
    private String _endTimeHours;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "startTimeHours")
    private String _startTimeHours;
    @Json(name = "scheduleStartDate")
    private String _scheduleStartDate;
    @Json(name = "scheduleStartTime")
    private String _scheduleStartTime;
    @Json(name = "scheduleEndDate")
    private String _scheduleEndDate;
    @Json(name = "scheduleEndTime")
    private String _scheduleEndTime;

    public Schedule() {
    }

    public String getEndTime() {
        if (_endTime == null)
            return _scheduleEndTime;

        return _endTime;
    }

    public String getEndTimeHours() {
        return _endTimeHours;
    }

    public String getStartTime() {
        if (_startTime == null)
            return _scheduleStartTime;
        return _startTime;
    }

    public String getStartTimeHours() {
        return _startTimeHours;
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
    public Schedule(String startTime) {
        _startTime = startTime;
        _endTime = null;
    }

    public Schedule(String startTime, String endTime) {
        _startTime = startTime;
        _endTime = endTime;
    }

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

    public boolean isExact() {
        return misc.isEmptyOrNull(getEndTime());
    }

    /*-*********************************************-*/
	/*-			Parcelable Implementation			-*/
	/*-*********************************************-*/
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

        @Override
        public Schedule createFromParcel(Parcel source) {
            try {
                return Schedule.fromJson(new JsonObject(source.readString()));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toJson().toString());
    }

}

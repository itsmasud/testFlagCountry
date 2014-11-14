package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

import java.text.ParseException;

public class LoggedWork implements Parcelable {
    @Json(name = "checkinLat")
    private Double _checkinLat;
    @Json(name = "checkinLng")
    private Double _checkinLng;
    @Json(name = "checkoutLat")
    private Double _checkoutLat;
    @Json(name = "checkoutLng")
    private Double _checkoutLng;
    @Json(name = "endDate")
    private String _endDate;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "hours")
    private Double _hours;
    @Json(name = "hoursType")
    private Integer _hoursType;
    @Json(name = "loggedHoursId")
    private Long _loggedHoursId;
    @Json(name = "noOfDevices")
    private Integer _noOfDevices;
    @Json(name = "startDate")
    private String _startDate;
    @Json(name = "startTime")
    private String _startTime;

    public LoggedWork() {
    }

    public Double getCheckinLat() {
        return _checkinLat;
    }

    public Double getCheckinLng() {
        return _checkinLng;
    }

    public Double getCheckoutLat() {
        return _checkoutLat;
    }

    public Double getCheckoutLng() {
        return _checkoutLng;
    }

    public String getEndDate() {
        return _endDate;
    }

    public String getEndTime() {
        return _endTime;
    }

    public Double getHours() {
        return _hours;
    }

    public Integer getHoursType() {
        return _hoursType;
    }

    public Long getLoggedHoursId() {
        return _loggedHoursId;
    }

    public Integer getNoOfDevices() {
        return _noOfDevices;
    }

    public String getStartDate() {
        return _startDate;
    }

    public String getStartTime() {
        return _startTime;
    }

    public JsonObject toJson() {
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

    /*-*********************************************-*/
    /*-             Human Generated Code            -*/
    /*-*********************************************-*/
    public static final Creator<LoggedWork> CREATOR = new Creator<LoggedWork>() {

        @Override
        public LoggedWork createFromParcel(Parcel source) {
            try {
                return LoggedWork.fromJson(new JsonObject(source.readString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public LoggedWork[] newArray(int size) {
            return new LoggedWork[size];
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

package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LoggedWork implements Parcelable {
    private static final String TAG = "LoggedWork";

    @Json(name = "checkInDistance")
    private Double _checkInDistance;
    @Json(name = "checkOutDistance")
    private Double _checkOutDistance;
    @Json(name = "checkinHoursId")
    private Integer _checkinHoursId;
    @Json(name = "checkinLat")
    private Double _checkinLat;
    @Json(name = "checkinLng")
    private Double _checkinLng;
    @Json(name = "checkoutLat")
    private Double _checkoutLat;
    @Json(name = "checkoutLng")
    private Double _checkoutLng;
    @Json(name = "date2")
    private String _date2;
    @Json(name = "date2Formatted")
    private String _date2Formatted;
    @Json(name = "endDate")
    private String _endDate;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "endTimeEdit")
    private String _endTimeEdit;
    @Json(name = "hours")
    private Double _hours;
    @Json(name = "hoursType")
    private Integer _hoursType;
    @Json(name = "loggedHoursId")
    private Long _loggedHoursId;
    @Json(name = "noOfDevices")
    private Integer _noOfDevices;
    @Json(name = "noOfDevicesEdit")
    private Integer _noOfDevicesEdit;
    @Json(name = "startDate")
    private String _startDate;
    @Json(name = "startDateEdit")
    private String _startDateEdit;
    @Json(name = "startDateFormatted")
    private String _startDateFormatted;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "startTimeEdit")
    private String _startTimeEdit;

    public LoggedWork() {
    }

    public Double getCheckInDistance() {
        return _checkInDistance;
    }

    public Double getCheckOutDistance() {
        return _checkOutDistance;
    }

    public Integer getCheckinHoursId() {
        return _checkinHoursId;
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

    public String getDate2() {
        return _date2;
    }

    public String getDate2Formatted() {
        return _date2Formatted;
    }

    public String getEndDate() {
        return _endDate;
    }

    public String getEndTime() {
        return _endTime;
    }

    public String getEndTimeEdit() {
        return _endTimeEdit;
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

    public Integer getNoOfDevicesEdit() {
        return _noOfDevicesEdit;
    }

    public String getStartDate() {
        return _startDate;
    }

    public String getStartDateEdit() {
        return _startDateEdit;
    }

    public String getStartDateFormatted() {
        return _startDateFormatted;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getStartTimeEdit() {
        return _startTimeEdit;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LoggedWork loggedWork) {
        try {
            return Serializer.serializeObject(loggedWork);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static LoggedWork fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(LoggedWork.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                return LoggedWork.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
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
        dest.writeParcelable(toJson(), flags);
    }
}

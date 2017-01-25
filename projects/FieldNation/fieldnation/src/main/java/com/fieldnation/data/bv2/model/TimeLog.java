package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLog {
    private static final String TAG = "TimeLog";

    @Json(name = "logged_by")
    private User _loggedBy;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "in")
    private CheckInOut _in;

    @Json(name = "devices")
    private Double _devices;

    @Json(name = "verified")
    private Boolean _verified;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "was_late")
    private Boolean _wasLate;

    @Json(name = "out")
    private CheckInOut _out;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    public TimeLog() {
    }

    public User getLoggedBy() {
        return _loggedBy;
    }

    public Double getHours() {
        return _hours;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public CheckInOut getIn() {
        return _in;
    }

    public Double getDevices() {
        return _devices;
    }

    public Boolean getVerified() {
        return _verified;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Boolean getWasLate() {
        return _wasLate;
    }

    public CheckInOut getOut() {
        return _out;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLog fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLog.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeLog timeLog) {
        try {
            return Serializer.serializeObject(timeLog);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

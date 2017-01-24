package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLog {
    private static final String TAG = "TimeLog";

    @Json(name = "logged_by")
    private User loggedBy;

    @Json(name = "hours")
    private Double hours;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    @Json(name = "in")
    private CheckInOut in;

    @Json(name = "devices")
    private Double devices;

    @Json(name = "verified")
    private Boolean verified;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "was_late")
    private Boolean wasLate;

    @Json(name = "out")
    private CheckInOut out;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay;

    public TimeLog() {
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public Double getHours() {
        return hours;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public CheckInOut getIn() {
        return in;
    }

    public Double getDevices() {
        return devices;
    }

    public Boolean getVerified() {
        return verified;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public Boolean getWasLate() {
        return wasLate;
    }

    public CheckInOut getOut() {
        return out;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLog fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLog.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

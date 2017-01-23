package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLog {
    private static final String TAG = "TimeLog";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "in")
    private CheckInOut in = null;

    @Json(name = "out")
    private CheckInOut out = null;

    @Json(name = "devices")
    private Double devices = null;

    @Json(name = "hours")
    private Double hours = null;

    @Json(name = "verified")
    private Boolean verified = null;

    @Json(name = "was_late")
    private Boolean wasLate = null;

    @Json(name = "logged_by")
    private User loggedBy = null;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public TimeLog() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public CheckInOut getIn() {
        return in;
    }

    public CheckInOut getOut() {
        return out;
    }

    public Double getDevices() {
        return devices;
    }

    public Double getHours() {
        return hours;
    }

    public Boolean getVerified() {
        return verified;
    }

    public Boolean getWasLate() {
        return wasLate;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    public ActionsEnum[] getActions() {
        return actions;
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
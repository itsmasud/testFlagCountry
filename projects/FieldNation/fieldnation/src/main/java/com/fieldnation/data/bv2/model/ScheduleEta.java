package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleEta {
    public static final String TAG = "ScheduleEta";

    @Json(name = "user")
    private User user = null;

    @Json(name = "mode")
    private Boolean mode = null;

    @Json(name = "start")
    private String start = null;

    @Json(name = "end")
    private String end = null;

    @Json(name = "hour_estimate")
    private Double hourEstimate = null;

    @Json(name = "status")
    private ScheduleEtaStatus status = null;

    public ScheduleEta() {
    }

    public User getUser() {
        return user;
    }

    public Boolean getMode() {
        return mode;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Double getHourEstimate() {
        return hourEstimate;
    }

    public ScheduleEtaStatus getStatus() {
        return status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleEta fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEta.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleEta scheduleEta) {
        try {
            return Serializer.serializeObject(scheduleEta);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
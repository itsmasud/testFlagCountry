package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleEta {
    private static final String TAG = "ScheduleEta";

    @Json(name = "mode")
    private Boolean mode;

    @Json(name = "hour_estimate")
    private Double hourEstimate;

    @Json(name = "start")
    private Date start;

    @Json(name = "end")
    private Date end;

    @Json(name = "user")
    private User user;

    @Json(name = "status")
    private ScheduleEtaStatus status;

    public ScheduleEta() {
    }

    public Boolean getMode() {
        return mode;
    }

    public Double getHourEstimate() {
        return hourEstimate;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public User getUser() {
        return user;
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

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleEta {
    private static final String TAG = "ScheduleEta";

    @Json(name = "mode")
    private Boolean _mode;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "start")
    private Date _start;

    @Json(name = "end")
    private Date _end;

    @Json(name = "user")
    private User _user;

    @Json(name = "status")
    private ScheduleEtaStatus _status;

    public ScheduleEta() {
    }

    public Boolean getMode() {
        return _mode;
    }

    public Double getHourEstimate() {
        return _hourEstimate;
    }

    public Date getStart() {
        return _start;
    }

    public Date getEnd() {
        return _end;
    }

    public User getUser() {
        return _user;
    }

    public ScheduleEtaStatus getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleEta fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEta.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

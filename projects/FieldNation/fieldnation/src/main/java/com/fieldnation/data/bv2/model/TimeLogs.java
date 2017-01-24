package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLogs {
    private static final String TAG = "TimeLogs";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "hours")
    private Double hours;

    @Json(name = "should_verify")
    private Boolean shouldVerify;

    @Json(name = "onmyway")
    private OnMyWay onmyway;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "confirmed")
    private Date confirmed;

    @Json(name = "actions")
    private ActionsEnum actions;

    @Json(name = "results")
    private TimeLog[] results;

    @Json(name = "status")
    private String status;

    public TimeLogs() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Double getHours() {
        return hours;
    }

    public Boolean getShouldVerify() {
        return shouldVerify;
    }

    public OnMyWay getOnmyway() {
        return onmyway;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Date getConfirmed() {
        return confirmed;
    }

    public ActionsEnum getActions() {
        return actions;
    }

    public TimeLog[] getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLogs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLogs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeLogs timeLogs) {
        try {
            return Serializer.serializeObject(timeLogs);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

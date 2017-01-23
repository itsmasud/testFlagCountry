package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLogs {
    private static final String TAG = "";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "should_verify")
    private Boolean shouldVerify = null;

    @Json(name = "hours")
    private Double hours = null;

    @Json(name = "status")
    private String status = null;

    @Json(name = "confirmed")
    private TimeLogsConfirmed confirmed = null;

    @Json(name = "onmyway")
    private OnMyWay onmyway = null;

    @Json(name = "actions")
    private ActionsEnum actions = null;

    @Json(name = "results")
    private TimeLog[] results;

    public enum ActionsEnum {
        @Json(name = "create")
        CREATE("create");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public TimeLogs() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Boolean getShouldVerify() {
        return shouldVerify;
    }

    public Double getHours() {
        return hours;
    }

    public String getStatus() {
        return status;
    }

    public TimeLogsConfirmed getConfirmed() {
        return confirmed;
    }

    public OnMyWay getOnmyway() {
        return onmyway;
    }

    public ActionsEnum getActions() {
        return actions;
    }

    public TimeLog[] getResults() {
        return results;
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
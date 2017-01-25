package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLogs {
    private static final String TAG = "TimeLogs";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "should_verify")
    private Boolean _shouldVerify;

    @Json(name = "onmyway")
    private OnMyWay _onmyway;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "confirmed")
    private Date _confirmed;

    @Json(name = "actions")
    private ActionsEnum _actions;

    @Json(name = "results")
    private TimeLog[] _results;

    @Json(name = "status")
    private String _status;

    public TimeLogs() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Double getHours() {
        return _hours;
    }

    public Boolean getShouldVerify() {
        return _shouldVerify;
    }

    public OnMyWay getOnmyway() {
        return _onmyway;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Date getConfirmed() {
        return _confirmed;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public TimeLog[] getResults() {
        return _results;
    }

    public String getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLogs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLogs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

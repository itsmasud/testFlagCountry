package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeLogsConfirmed {
    private static final String TAG = "TimeLogsConfirmed";

    // TODO not sure of the datatype
    @Json(name = "utc")
    private Long utc = null;

    @Json(name = "local")
    private Local local = null;

    public TimeLogsConfirmed() {
    }

    public Long getUtc() {
        return utc;
    }

    public Local getLocal() {
        return local;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLogsConfirmed fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLogsConfirmed.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeLogsConfirmed timeLogsConfirmed) {
        try {
            return Serializer.serializeObject(timeLogsConfirmed);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
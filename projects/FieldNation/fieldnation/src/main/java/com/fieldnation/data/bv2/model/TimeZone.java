package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeZone {
    private static final String TAG = "TimeZone";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "short")
    private String _short = null;

    @Json(name = "offset")
    private Double offset = null;

    public TimeZone() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShort() {
        return _short;
    }

    public Double getOffset() {
        return offset;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeZone fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeZone.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeZone timeZone) {
        try {
            return Serializer.serializeObject(timeZone);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
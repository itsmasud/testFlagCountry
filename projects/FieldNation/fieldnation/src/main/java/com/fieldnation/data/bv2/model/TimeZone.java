package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TimeZone {
    private static final String TAG = "TimeZone";

    @Json(name = "offset")
    private Double _offset;

    @Json(name = "name")
    private String _name;

    @Json(name = "short")
    private String _short;

    @Json(name = "id")
    private Integer _id;

    public TimeZone() {
    }

    public Double getOffset() {
        return _offset;
    }

    public String getName() {
        return _name;
    }

    public String getShort() {
        return _short;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeZone fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeZone.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

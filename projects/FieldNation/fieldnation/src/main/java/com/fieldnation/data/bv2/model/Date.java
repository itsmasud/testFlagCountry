package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Date {
    private static final String TAG = "Date";

    @Json(name = "utc")
    private Long _utc;

    @Json(name = "local")
    private Local _local;

    public Date() {
    }

    public Long getUtc() {
        return _utc;
    }

    public Local getLocal() {
        return _local;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Date fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Date.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Date date) {
        try {
            return Serializer.serializeObject(date);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

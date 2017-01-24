package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Local {
    private static final String TAG = "Local";

    @Json(name = "date")
    private String date;

    @Json(name = "time")
    private String time;

    public Local() {
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Local fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Local.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Local local) {
        try {
            return Serializer.serializeObject(local);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

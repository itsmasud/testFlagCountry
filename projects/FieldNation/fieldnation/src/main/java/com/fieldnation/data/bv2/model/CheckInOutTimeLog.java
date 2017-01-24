package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CheckInOutTimeLog {
    private static final String TAG = "CheckInOutTimeLog";

    @Json(name = "id")
    private Integer id;

    public CheckInOutTimeLog() {
    }

    public Integer getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CheckInOutTimeLog fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CheckInOutTimeLog.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CheckInOutTimeLog checkInOutTimeLog) {
        try {
            return Serializer.serializeObject(checkInOutTimeLog);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

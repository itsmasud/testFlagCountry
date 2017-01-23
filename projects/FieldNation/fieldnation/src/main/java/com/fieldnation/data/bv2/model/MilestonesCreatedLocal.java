package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MilestonesCreatedLocal {
    private static final String TAG = "MilestonesCreatedLocal";

    @Json(name = "date")
    private String date = null;

    @Json(name = "time")
    private String time = null;

    public MilestonesCreatedLocal() {
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
    public static MilestonesCreatedLocal fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MilestonesCreatedLocal.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MilestonesCreatedLocal milestonesCreatedLocal) {
        try {
            return Serializer.serializeObject(milestonesCreatedLocal);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
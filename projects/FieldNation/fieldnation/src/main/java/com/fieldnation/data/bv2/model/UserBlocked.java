package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserBlocked {
    private static final String TAG = "UserBlocked";

    @Json(name = "at")
    private String at;

    @Json(name = "by")
    private UserBlockedBy by;

    public UserBlocked() {
    }

    public String getAt() {
        return at;
    }

    public UserBlockedBy getBy() {
        return by;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserBlocked fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlocked.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserBlocked userBlocked) {
        try {
            return Serializer.serializeObject(userBlocked);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

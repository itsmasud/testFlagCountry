package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserBlockedBy {
    private static final String TAG = "UserBlockedBy";

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public UserBlockedBy() {
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserBlockedBy fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlockedBy.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserBlockedBy userBlockedBy) {
        try {
            return Serializer.serializeObject(userBlockedBy);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

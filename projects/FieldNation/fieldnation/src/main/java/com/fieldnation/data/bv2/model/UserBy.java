package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserBy {
    private static final String TAG = "UserBy";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    public UserBy() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserBy fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBy.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserBy userBy) {
        try {
            return Serializer.serializeObject(userBy);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
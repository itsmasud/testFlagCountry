package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Hold {
    private static final String TAG = "Hold";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "reason")
    private String reason = null;

    @Json(name = "name")
    private String name = null;

    public Hold() {
    }

    public Integer getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getName() {
        return name;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Hold fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Hold.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Hold hold) {
        try {
            return Serializer.serializeObject(hold);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


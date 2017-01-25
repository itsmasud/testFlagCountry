package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Hold {
    private static final String TAG = "Hold";

    @Json(name = "reason")
    private String _reason;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public Hold() {
    }

    public String getReason() {
        return _reason;
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
    public static Hold fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Hold.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

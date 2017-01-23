package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Json3 {
    private static final String TAG = "Json3";

    @Json(name = "email")
    private String email = null;

    public Json3() {
    }

    public String getEmail() {
        return email;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Json3 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Json3.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Json3 json3) {
        try {
            return Serializer.serializeObject(json3);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


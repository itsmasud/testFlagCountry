package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Json2 {
    private static final String TAG = "Json2";

    @Json(name = "pin")
    private String pin = null;

    public Json2() {
    }

    public String getPin() {
        return pin;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Json2 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Json2.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Json2 json2) {
        try {
            return Serializer.serializeObject(json2);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


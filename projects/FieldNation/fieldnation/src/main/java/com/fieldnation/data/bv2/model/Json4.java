package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Json4 {
    private static final String TAG = "Json4";

    @Json(name = "preference_value")
    private Integer preferenceValue = null;

    public Json4() {
    }

    public Integer getPreferenceValue() {
        return preferenceValue;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Json4 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Json4.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Json4 json4) {
        try {
            return Serializer.serializeObject(json4);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


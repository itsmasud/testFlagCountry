package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Json1 {
    private static final String TAG = "Json1";

    @Json(name = "country")
    private String country = null;

    @Json(name = "phone")
    private String phone = null;

    public Json1() {
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Json1 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Json1.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Json1 json1) {
        try {
            return Serializer.serializeObject(json1);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


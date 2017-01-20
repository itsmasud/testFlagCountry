package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryAddress1 {
    private static final String TAG = "CountryAddress1";

    @Json(name = "required")
    private Boolean required = null;

    @Json(name = "label")
    private String label = null;

    public CountryAddress1() {
    }

    public Boolean getRequired() {
        return required;
    }

    public String getLabel() {
        return label;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryAddress1 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryAddress1.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryAddress1 countryAddress1) {
        try {
            return Serializer.serializeObject(countryAddress1);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
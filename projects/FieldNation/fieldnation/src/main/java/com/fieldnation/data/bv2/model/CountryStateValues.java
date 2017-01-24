package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryStateValues {
    private static final String TAG = "CountryStateValues";

    @Json(name = "label")
    private String label;

    @Json(name = "value")
    private String value;

    public CountryStateValues() {
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryStateValues fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryStateValues.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryStateValues countryStateValues) {
        try {
            return Serializer.serializeObject(countryStateValues);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

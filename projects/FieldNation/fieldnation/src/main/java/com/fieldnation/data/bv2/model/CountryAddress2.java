package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryAddress2 {
    private static final String TAG = "CountryAddress2";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    public CountryAddress2() {
    }

    public String getLabel() {
        return _label;
    }

    public Boolean getRequired() {
        return _required;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryAddress2 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryAddress2.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryAddress2 countryAddress2) {
        try {
            return Serializer.serializeObject(countryAddress2);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

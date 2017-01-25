package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryZip {
    private static final String TAG = "CountryZip";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    public CountryZip() {
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
    public static CountryZip fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryZip.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryZip countryZip) {
        try {
            return Serializer.serializeObject(countryZip);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

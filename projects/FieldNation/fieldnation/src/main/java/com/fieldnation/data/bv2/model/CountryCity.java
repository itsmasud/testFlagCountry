package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryCity {
    private static final String TAG = "CountryCity";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    public CountryCity() {
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
    public static CountryCity fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryCity.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryCity countryCity) {
        try {
            return Serializer.serializeObject(countryCity);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

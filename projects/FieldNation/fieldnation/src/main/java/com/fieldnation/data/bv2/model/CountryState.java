package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryState {
    private static final String TAG = "CountryState";

    @Json(name = "required")
    private Boolean required = null;

    @Json(name = "label")
    private String label = null;

    @Json(name = "values")
    private CountryStateValues[] values;

    public CountryState() {
    }

    public Boolean getRequired() {
        return required;
    }

    public String getLabel() {
        return label;
    }

    public CountryStateValues[] getValues() {
        return values;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryState fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryState.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryState countryState) {
        try {
            return Serializer.serializeObject(countryState);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


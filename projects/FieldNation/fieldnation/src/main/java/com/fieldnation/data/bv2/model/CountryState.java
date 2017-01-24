package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CountryState {
    private static final String TAG = "CountryState";

    @Json(name = "values")
    private CountryStateValues[] values;

    @Json(name = "label")
    private String label;

    @Json(name = "required")
    private Boolean required;

    public CountryState() {
    }

    public CountryStateValues[] getValues() {
        return values;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getRequired() {
        return required;
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

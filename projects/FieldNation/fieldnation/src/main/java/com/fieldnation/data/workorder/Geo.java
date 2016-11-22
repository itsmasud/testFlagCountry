package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Geo {
    private static final String TAG = "Geo";

    @Json(name = "latitude")
    private Double _latitude;
    @Json(name = "longitude")
    private Double _longitude;
    @Json(name = "obfuscated")
    private Boolean _obfuscated;
    @Json(name = "precise")
    private Boolean _precise;

    public Geo() {
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public Boolean getObfuscated() {
        return _obfuscated;
    }

    public Boolean getPrecise() {
        return _precise;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Geo geo) {
        try {
            return Serializer.serializeObject(geo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Geo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Geo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Geo {
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
            ex.printStackTrace();
            return null;
        }
    }

    public static Geo fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Geo.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

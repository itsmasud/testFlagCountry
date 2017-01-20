package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Coords {
    private static final String TAG = "Coords";

    @Json(name = "latitude")
    private Double latitude = null;

    @Json(name = "longitude")
    private Double longitude = null;

    @Json(name = "exact")
    private Boolean exact = null;

    @Json(name = "success")
    private Boolean success = null;

    @Json(name = "search")
    private String search = null;

    @Json(name = "distance")
    private Double distance = null;

    public Coords() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getExact() {
        return exact;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getSearch() {
        return search;
    }

    public Double getDistance() {
        return distance;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Coords fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Coords.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Coords coords) {
        try {
            return Serializer.serializeObject(coords);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Coords {
    private static final String TAG = "Coords";

    @Json(name = "search")
    private String search;

    @Json(name = "distance")
    private Double distance;

    @Json(name = "success")
    private Boolean success;

    @Json(name = "latitude")
    private Double latitude;

    @Json(name = "exact")
    private Boolean exact;

    @Json(name = "longitude")
    private Double longitude;

    public Coords() {
    }

    public String getSearch() {
        return search;
    }

    public Double getDistance() {
        return distance;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Boolean getExact() {
        return exact;
    }

    public Double getLongitude() {
        return longitude;
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

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Coords {
    private static final String TAG = "Coords";

    @Json(name = "search")
    private String _search;

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "success")
    private Boolean _success;

    @Json(name = "latitude")
    private Double _latitude;

    @Json(name = "exact")
    private Boolean _exact;

    @Json(name = "longitude")
    private Double _longitude;

    public Coords() {
    }

    public String getSearch() {
        return _search;
    }

    public Double getDistance() {
        return _distance;
    }

    public Boolean getSuccess() {
        return _success;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Boolean getExact() {
        return _exact;
    }

    public Double getLongitude() {
        return _longitude;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Coords fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Coords.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

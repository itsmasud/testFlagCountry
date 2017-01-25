package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationCoordinates {
    private static final String TAG = "LocationCoordinates";

    @Json(name = "zip")
    private String _zip;

    @Json(name = "country")
    private String _country;

    @Json(name = "city")
    private String _city;

    @Json(name = "latitude")
    private Double _latitude;

    @Json(name = "exact")
    private Boolean _exact;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "state")
    private String _state;

    @Json(name = "type")
    private String _type;

    @Json(name = "longitude")
    private Double _longitude;

    public LocationCoordinates() {
    }

    public String getZip() {
        return _zip;
    }

    public String getCountry() {
        return _country;
    }

    public String getCity() {
        return _city;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Boolean getExact() {
        return _exact;
    }

    public Integer getId() {
        return _id;
    }

    public String getState() {
        return _state;
    }

    public String getType() {
        return _type;
    }

    public Double getLongitude() {
        return _longitude;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationCoordinates fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationCoordinates.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationCoordinates locationCoordinates) {
        try {
            return Serializer.serializeObject(locationCoordinates);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

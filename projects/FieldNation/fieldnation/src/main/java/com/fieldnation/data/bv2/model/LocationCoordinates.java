package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationCoordinates {
    private static final String TAG = "LocationCoordinates";

    @Json(name = "type")
    private String type = null;

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "latitude")
    private Double latitude = null;

    @Json(name = "longitude")
    private Double longitude = null;

    @Json(name = "exact")
    private Boolean exact = null;

    @Json(name = "city")
    private String city = null;

    @Json(name = "state")
    private String state = null;

    @Json(name = "zip")
    private String zip = null;

    @Json(name = "country")
    private String country = null;

    public LocationCoordinates() {
    }

    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
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

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationCoordinates fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationCoordinates.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}


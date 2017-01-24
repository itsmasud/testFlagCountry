package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationCoordinates {
    private static final String TAG = "LocationCoordinates";

    @Json(name = "zip")
    private String zip;

    @Json(name = "country")
    private String country;

    @Json(name = "city")
    private String city;

    @Json(name = "latitude")
    private Double latitude;

    @Json(name = "exact")
    private Boolean exact;

    @Json(name = "id")
    private Integer id;

    @Json(name = "state")
    private String state;

    @Json(name = "type")
    private String type;

    @Json(name = "longitude")
    private Double longitude;

    public LocationCoordinates() {
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Boolean getExact() {
        return exact;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public Double getLongitude() {
        return longitude;
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

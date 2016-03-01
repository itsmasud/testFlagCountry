package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Provider {
    private static final String TAG = "Provider";

    @Json(name = "cell")
    private Double _cell;
    @Json(name = "city")
    private String _city;
    @Json(name = "distance")
    private Double _distance;
    @Json(name = "email")
    private String _email;
    @Json(name = "ext")
    private String _ext;
    @Json(name = "firstName")
    private String _firstName;
    @Json(name = "firstname")
    private String _firstname;
    @Json(name = "lastName")
    private String _lastName;
    @Json(name = "lastname")
    private String _lastname;
    @Json(name = "latitude")
    private Double _latitude;
    @Json(name = "longitude")
    private Double _longitude;
    @Json(name = "phone")
    private Double _phone;
    @Json(name = "photo")
    private Photo _photo;
    @Json(name = "photoThumbUrl")
    private String _photoThumbUrl;
    @Json(name = "photoUrl")
    private String _photoUrl;
    @Json(name = "state")
    private String _state;
    @Json(name = "userId")
    private Integer _userId;
    @Json(name = "username")
    private String _username;
    @Json(name = "zip")
    private Integer _zip;

    public Provider() {
    }

    public Double getCell() {
        return _cell;
    }

    public String getCity() {
        return _city;
    }

    public Double getDistance() {
        return _distance;
    }

    public String getEmail() {
        return _email;
    }

    public String getExt() {
        return _ext;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getFirstname() {
        return _firstname;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getLastname() {
        return _lastname;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public Double getPhone() {
        return _phone;
    }

    public Photo getPhoto() {
        return _photo;
    }

    public String getPhotoThumbUrl() {
        return _photoThumbUrl;
    }

    public String getPhotoUrl() {
        return _photoUrl;
    }

    public String getState() {
        return _state;
    }

    public Integer getUserId() {
        return _userId;
    }

    public String getUsername() {
        return _username;
    }

    public Integer getZip() {
        return _zip;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Provider provider) {
        try {
            return Serializer.serializeObject(provider);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Provider fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Provider.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
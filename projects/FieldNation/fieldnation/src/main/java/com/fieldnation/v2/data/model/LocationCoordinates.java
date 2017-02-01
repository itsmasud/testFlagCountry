package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class LocationCoordinates implements Parcelable {
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

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getZip() {
        return _zip;
    }

    public LocationCoordinates zip(String zip) {
        _zip = zip;
        return this;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getCountry() {
        return _country;
    }

    public LocationCoordinates country(String country) {
        _country = country;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public LocationCoordinates city(String city) {
        _city = city;
        return this;
    }

    public void setLatitude(Double latitude) {
        _latitude = latitude;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public LocationCoordinates latitude(Double latitude) {
        _latitude = latitude;
        return this;
    }

    public void setExact(Boolean exact) {
        _exact = exact;
    }

    public Boolean getExact() {
        return _exact;
    }

    public LocationCoordinates exact(Boolean exact) {
        _exact = exact;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public LocationCoordinates id(Integer id) {
        _id = id;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public LocationCoordinates state(String state) {
        _state = state;
        return this;
    }

    public void setType(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

    public LocationCoordinates type(String type) {
        _type = type;
        return this;
    }

    public void setLongitude(Double longitude) {
        _longitude = longitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public LocationCoordinates longitude(Double longitude) {
        _longitude = longitude;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationCoordinates[] fromJsonArray(JsonArray array) {
        LocationCoordinates[] list = new LocationCoordinates[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<LocationCoordinates> CREATOR = new Parcelable.Creator<LocationCoordinates>() {

        @Override
        public LocationCoordinates createFromParcel(Parcel source) {
            try {
                return LocationCoordinates.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationCoordinates[] newArray(int size) {
            return new LocationCoordinates[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}

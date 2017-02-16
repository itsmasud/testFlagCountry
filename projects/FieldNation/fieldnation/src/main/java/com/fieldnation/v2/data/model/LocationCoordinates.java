package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class LocationCoordinates implements Parcelable {
    private static final String TAG = "LocationCoordinates";

    @Json(name = "city")
    private String _city;

    @Json(name = "country")
    private String _country;

    @Json(name = "exact")
    private Boolean _exact;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "latitude")
    private Double _latitude;

    @Json(name = "longitude")
    private Double _longitude;

    @Json(name = "state")
    private String _state;

    @Json(name = "type")
    private String _type;

    @Json(name = "zip")
    private String _zip;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public LocationCoordinates() {
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        return _city;
    }

    public LocationCoordinates city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setCountry(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
    }

    public String getCountry() {
        return _country;
    }

    public LocationCoordinates country(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
        return this;
    }

    public void setExact(Boolean exact) throws ParseException {
        _exact = exact;
        SOURCE.put("exact", exact);
    }

    public Boolean getExact() {
        return _exact;
    }

    public LocationCoordinates exact(Boolean exact) throws ParseException {
        _exact = exact;
        SOURCE.put("exact", exact);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public LocationCoordinates id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLatitude(Double latitude) throws ParseException {
        _latitude = latitude;
        SOURCE.put("latitude", latitude);
    }

    public Double getLatitude() {
        return _latitude;
    }

    public LocationCoordinates latitude(Double latitude) throws ParseException {
        _latitude = latitude;
        SOURCE.put("latitude", latitude);
        return this;
    }

    public void setLongitude(Double longitude) throws ParseException {
        _longitude = longitude;
        SOURCE.put("longitude", longitude);
    }

    public Double getLongitude() {
        return _longitude;
    }

    public LocationCoordinates longitude(Double longitude) throws ParseException {
        _longitude = longitude;
        SOURCE.put("longitude", longitude);
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        return _state;
    }

    public LocationCoordinates state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        return _type;
    }

    public LocationCoordinates type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    public void setZip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public String getZip() {
        return _zip;
    }

    public LocationCoordinates zip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationCoordinates[] array) {
        JsonArray list = new JsonArray();
        for (LocationCoordinates item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

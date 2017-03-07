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
    private JsonObject SOURCE;

    public LocationCoordinates() {
        SOURCE = new JsonObject();
    }

    public LocationCoordinates(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        try {
            if (_city != null)
                return _city;

            if (SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_country != null)
                return _country;

            if (SOURCE.has("country") && SOURCE.get("country") != null)
                _country = SOURCE.getString("country");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_exact != null)
                return _exact;

            if (SOURCE.has("exact") && SOURCE.get("exact") != null)
                _exact = SOURCE.getBoolean("exact");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_latitude != null)
                return _latitude;

            if (SOURCE.has("latitude") && SOURCE.get("latitude") != null)
                _latitude = SOURCE.getDouble("latitude");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_longitude != null)
                return _longitude;

            if (SOURCE.has("longitude") && SOURCE.get("longitude") != null)
                _longitude = SOURCE.getDouble("longitude");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_state != null)
                return _state;

            if (SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = SOURCE.getString("type");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_zip != null)
                return _zip;

            if (SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getString("zip");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
            return new LocationCoordinates(obj);
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

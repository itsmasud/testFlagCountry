package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Coords implements Parcelable {
    private static final String TAG = "Coords";

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "exact")
    private Boolean _exact;

    @Json(name = "latitude")
    private Double _latitude;

    @Json(name = "longitude")
    private Double _longitude;

    @Json(name = "search")
    private String _search;

    @Json(name = "success")
    private Boolean _success;

    @Source
    private JsonObject SOURCE;

    public Coords() {
        SOURCE = new JsonObject();
    }

    public Coords(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDistance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
    }

    public Double getDistance() {
        try {
            if (_distance != null)
                return _distance;

            if (SOURCE.has("distance") && SOURCE.get("distance") != null)
                _distance = SOURCE.getDouble("distance");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _distance;
    }

    public Coords distance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
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

    public Coords exact(Boolean exact) throws ParseException {
        _exact = exact;
        SOURCE.put("exact", exact);
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

    public Coords latitude(Double latitude) throws ParseException {
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

    public Coords longitude(Double longitude) throws ParseException {
        _longitude = longitude;
        SOURCE.put("longitude", longitude);
        return this;
    }

    public void setSearch(String search) throws ParseException {
        _search = search;
        SOURCE.put("search", search);
    }

    public String getSearch() {
        try {
            if (_search != null)
                return _search;

            if (SOURCE.has("search") && SOURCE.get("search") != null)
                _search = SOURCE.getString("search");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _search;
    }

    public Coords search(String search) throws ParseException {
        _search = search;
        SOURCE.put("search", search);
        return this;
    }

    public void setSuccess(Boolean success) throws ParseException {
        _success = success;
        SOURCE.put("success", success);
    }

    public Boolean getSuccess() {
        try {
            if (_success != null)
                return _success;

            if (SOURCE.has("success") && SOURCE.get("success") != null)
                _success = SOURCE.getBoolean("success");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _success;
    }

    public Coords success(Boolean success) throws ParseException {
        _success = success;
        SOURCE.put("success", success);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Coords[] array) {
        JsonArray list = new JsonArray();
        for (Coords item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Coords[] fromJsonArray(JsonArray array) {
        Coords[] list = new Coords[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Coords fromJson(JsonObject obj) {
        try {
            return new Coords(obj);
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
    public static final Parcelable.Creator<Coords> CREATOR = new Parcelable.Creator<Coords>() {

        @Override
        public Coords createFromParcel(Parcel source) {
            try {
                return Coords.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Coords[] newArray(int size) {
            return new Coords[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    public Coords(double latitude, double longitude) throws ParseException {
        this();
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Coords(android.location.Location location) throws ParseException {
        this(location.getLatitude(), location.getLongitude());
    }
}

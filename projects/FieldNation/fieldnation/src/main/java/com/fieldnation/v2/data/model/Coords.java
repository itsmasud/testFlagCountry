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

public class Coords implements Parcelable {
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

    public void setSearch(String search) {
        _search = search;
    }

    public String getSearch() {
        return _search;
    }

    public Coords search(String search) {
        _search = search;
        return this;
    }

    public void setDistance(Double distance) {
        _distance = distance;
    }

    public Double getDistance() {
        return _distance;
    }

    public Coords distance(Double distance) {
        _distance = distance;
        return this;
    }

    public void setSuccess(Boolean success) {
        _success = success;
    }

    public Boolean getSuccess() {
        return _success;
    }

    public Coords success(Boolean success) {
        _success = success;
        return this;
    }

    public void setLatitude(Double latitude) {
        _latitude = latitude;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Coords latitude(Double latitude) {
        _latitude = latitude;
        return this;
    }

    public void setExact(Boolean exact) {
        _exact = exact;
    }

    public Boolean getExact() {
        return _exact;
    }

    public Coords exact(Boolean exact) {
        _exact = exact;
        return this;
    }

    public void setLongitude(Double longitude) {
        _longitude = longitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public Coords longitude(Double longitude) {
        _longitude = longitude;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Coords[] fromJsonArray(JsonArray array) {
        Coords[] list = new Coords[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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
        dest.writeParcelable(toJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    public Coords(double latitude, double longitude) {
        super();
        _latitude = latitude;
        _longitude = longitude;
    }

    public Coords(android.location.Location location) {
        this(location.getLatitude(), location.getLongitude());
    }
}

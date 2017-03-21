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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class LocationProviders implements Parcelable {
    private static final String TAG = "LocationProviders";

    @Json(name = "location_id")
    private Integer _locationId;

    @Json(name = "results")
    private User[] _results;

    @Source
    private JsonObject SOURCE;

    public LocationProviders() {
        SOURCE = new JsonObject();
    }

    public LocationProviders(JsonObject obj) {
        SOURCE = obj;
    }

    public void setLocationId(Integer locationId) throws ParseException {
        _locationId = locationId;
        SOURCE.put("location_id", locationId);
    }

    public Integer getLocationId() {
        try {
            if (_locationId == null && SOURCE.has("location_id") && SOURCE.get("location_id") != null)
                _locationId = SOURCE.getInt("location_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _locationId;
    }

    public LocationProviders locationId(Integer locationId) throws ParseException {
        _locationId = locationId;
        SOURCE.put("location_id", locationId);
        return this;
    }

    public void setResults(User[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", User.toJsonArray(results));
    }

    public User[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = User.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public LocationProviders results(User[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", User.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationProviders[] array) {
        JsonArray list = new JsonArray();
        for (LocationProviders item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static LocationProviders[] fromJsonArray(JsonArray array) {
        LocationProviders[] list = new LocationProviders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationProviders fromJson(JsonObject obj) {
        try {
            return new LocationProviders(obj);
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
    public static final Parcelable.Creator<LocationProviders> CREATOR = new Parcelable.Creator<LocationProviders>() {

        @Override
        public LocationProviders createFromParcel(Parcel source) {
            try {
                return LocationProviders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationProviders[] newArray(int size) {
            return new LocationProviders[size];
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

    public boolean isSet() {
        return true;
    }
}

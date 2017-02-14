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
 * Created by dmgen from swagger.
 */

public class LocationProviders implements Parcelable {
    private static final String TAG = "LocationProviders";

    @Json(name = "location_id")
    private Integer _locationId;

    @Json(name = "results")
    private User[] _results;

    public LocationProviders() {
    }

    public void setLocationId(Integer locationId) {
        _locationId = locationId;
    }

    public Integer getLocationId() {
        return _locationId;
    }

    public LocationProviders locationId(Integer locationId) {
        _locationId = locationId;
        return this;
    }

    public void setResults(User[] results) {
        _results = results;
    }

    public User[] getResults() {
        return _results;
    }

    public LocationProviders results(User[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationProviders[] fromJsonArray(JsonArray array) {
        LocationProviders[] list = new LocationProviders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationProviders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationProviders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationProviders locationProviders) {
        try {
            return Serializer.serializeObject(locationProviders);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

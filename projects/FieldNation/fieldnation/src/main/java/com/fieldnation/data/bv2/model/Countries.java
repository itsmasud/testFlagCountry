package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class Countries implements Parcelable {
    private static final String TAG = "Countries";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Country[] _results;

    public Countries() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Countries metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Country[] results) {
        _results = results;
    }

    public Country[] getResults() {
        return _results;
    }

    public Countries results(Country[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Countries fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Countries.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Countries countries) {
        try {
            return Serializer.serializeObject(countries);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Countries> CREATOR = new Parcelable.Creator<Countries>() {

        @Override
        public Countries createFromParcel(Parcel source) {
            try {
                return Countries.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Countries[] newArray(int size) {
            return new Countries[size];
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

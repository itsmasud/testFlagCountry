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

public class Holds implements Parcelable {
    private static final String TAG = "Holds";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Hold[] _results;

    public Holds() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Holds metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Hold[] results) {
        _results = results;
    }

    public Hold[] getResults() {
        return _results;
    }

    public Holds results(Hold[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Holds fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Holds.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Holds holds) {
        try {
            return Serializer.serializeObject(holds);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Holds> CREATOR = new Parcelable.Creator<Holds>() {

        @Override
        public Holds createFromParcel(Parcel source) {
            try {
                return Holds.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Holds[] newArray(int size) {
            return new Holds[size];
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

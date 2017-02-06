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

public class Pays implements Parcelable {
    private static final String TAG = "Pays";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Pay[] _results;

    public Pays() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Pays metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Pay[] results) {
        _results = results;
    }

    public Pay[] getResults() {
        return _results;
    }

    public Pays results(Pay[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Pays[] fromJsonArray(JsonArray array) {
        Pays[] list = new Pays[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Pays fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Pays.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Pays pays) {
        try {
            return Serializer.serializeObject(pays);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Pays> CREATOR = new Parcelable.Creator<Pays>() {

        @Override
        public Pays createFromParcel(Parcel source) {
            try {
                return Pays.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Pays[] newArray(int size) {
            return new Pays[size];
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

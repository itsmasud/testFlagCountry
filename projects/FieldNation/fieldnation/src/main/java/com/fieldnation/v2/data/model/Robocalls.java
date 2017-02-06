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

public class Robocalls implements Parcelable {
    private static final String TAG = "Robocalls";

    @Json(name = "results")
    private Robocall[] _results;

    public Robocalls() {
    }

    public void setResults(Robocall[] results) {
        _results = results;
    }

    public Robocall[] getResults() {
        return _results;
    }

    public Robocalls results(Robocall[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Robocalls[] fromJsonArray(JsonArray array) {
        Robocalls[] list = new Robocalls[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Robocalls fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Robocalls.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Robocalls robocalls) {
        try {
            return Serializer.serializeObject(robocalls);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Robocalls> CREATOR = new Parcelable.Creator<Robocalls>() {

        @Override
        public Robocalls createFromParcel(Parcel source) {
            try {
                return Robocalls.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Robocalls[] newArray(int size) {
            return new Robocalls[size];
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

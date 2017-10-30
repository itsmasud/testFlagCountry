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

public class Robocalls implements Parcelable {
    private static final String TAG = "Robocalls";

    @Json(name = "results")
    private Robocall[] _results;

    @Source
    private JsonObject SOURCE;

    public Robocalls() {
        SOURCE = new JsonObject();
    }

    public Robocalls(JsonObject obj) {
        SOURCE = obj;
    }

    public void setResults(Robocall[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Robocall.toJsonArray(results));
    }

    public Robocall[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Robocall.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new Robocall[0];

        return _results;
    }

    public Robocalls results(Robocall[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Robocall.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Robocalls[] array) {
        JsonArray list = new JsonArray();
        for (Robocalls item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Robocalls[] fromJsonArray(JsonArray array) {
        Robocalls[] list = new Robocalls[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Robocalls fromJson(JsonObject obj) {
        try {
            return new Robocalls(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}

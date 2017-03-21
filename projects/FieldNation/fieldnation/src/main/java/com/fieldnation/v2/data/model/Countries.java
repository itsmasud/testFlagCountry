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

public class Countries implements Parcelable {
    private static final String TAG = "Countries";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Country[] _results;

    @Source
    private JsonObject SOURCE;

    public Countries() {
        SOURCE = new JsonObject();
    }

    public Countries(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata != null && _metadata.isSet())
        return _metadata;

        return null;
    }

    public Countries metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Country[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Country.toJsonArray(results));
    }

    public Country[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Country.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public Countries results(Country[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Country.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Countries[] array) {
        JsonArray list = new JsonArray();
        for (Countries item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Countries[] fromJsonArray(JsonArray array) {
        Countries[] list = new Countries[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Countries fromJson(JsonObject obj) {
        try {
            return new Countries(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}

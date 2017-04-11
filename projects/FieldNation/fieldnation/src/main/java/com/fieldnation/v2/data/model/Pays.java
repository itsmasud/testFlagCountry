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

public class Pays implements Parcelable {
    private static final String TAG = "Pays";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Pay[] _results;

    @Source
    private JsonObject SOURCE;

    public Pays() {
        SOURCE = new JsonObject();
    }

    public Pays(JsonObject obj) {
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

    public Pays metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Pay[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Pay.toJsonArray(results));
    }

    public Pay[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Pay.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public Pays results(Pay[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Pay.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Pays[] array) {
        JsonArray list = new JsonArray();
        for (Pays item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Pays[] fromJsonArray(JsonArray array) {
        Pays[] list = new Pays[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Pays fromJson(JsonObject obj) {
        try {
            return new Pays(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}

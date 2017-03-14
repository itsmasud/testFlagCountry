package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Tasks implements Parcelable {
    private static final String TAG = "Tasks";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Task[] _results;

    @Source
    private JsonObject SOURCE;

    public Tasks() {
        SOURCE = new JsonObject();
    }

    public Tasks(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata != null)
                return _metadata;

            if (SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _metadata;
    }

    public Tasks metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Task[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Task.toJsonArray(results));
    }

    public Task[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Task.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public Tasks results(Task[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Task.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Tasks[] array) {
        JsonArray list = new JsonArray();
        for (Tasks item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Tasks[] fromJsonArray(JsonArray array) {
        Tasks[] list = new Tasks[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Tasks fromJson(JsonObject obj) {
        try {
            return new Tasks(obj);
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
    public static final Parcelable.Creator<Tasks> CREATOR = new Parcelable.Creator<Tasks>() {

        @Override
        public Tasks createFromParcel(Parcel source) {
            try {
                return Tasks.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
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

    public boolean isSet() {
        return true;
    }
}

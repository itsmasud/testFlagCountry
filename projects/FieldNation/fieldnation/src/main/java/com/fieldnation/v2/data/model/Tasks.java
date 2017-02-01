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

public class Tasks implements Parcelable {
    private static final String TAG = "Tasks";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Task[] _results;

    public Tasks() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Tasks metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Task[] results) {
        _results = results;
    }

    public Task[] getResults() {
        return _results;
    }

    public Tasks results(Task[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Tasks[] fromJsonArray(JsonArray array) {
        Tasks[] list = new Tasks[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Tasks fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Tasks.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Tasks tasks) {
        try {
            return Serializer.serializeObject(tasks);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

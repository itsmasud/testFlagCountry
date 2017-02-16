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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Holds implements Parcelable {
    private static final String TAG = "Holds";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Hold[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Holds() {
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Holds metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Hold[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Hold.toJsonArray(results));
    }

    public Hold[] getResults() {
        return _results;
    }

    public Holds results(Hold[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Hold.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Holds[] array) {
        JsonArray list = new JsonArray();
        for (Holds item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Holds[] fromJsonArray(JsonArray array) {
        Holds[] list = new Holds[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Holds fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Holds.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}

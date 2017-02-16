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

public class Signatures implements Parcelable {
    private static final String TAG = "Signatures";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Signature[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Signatures() {
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Signatures metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Signature[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Signature.toJsonArray(results));
    }

    public Signature[] getResults() {
        return _results;
    }

    public Signatures results(Signature[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Signature.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Signatures[] array) {
        JsonArray list = new JsonArray();
        for (Signatures item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Signatures[] fromJsonArray(JsonArray array) {
        Signatures[] list = new Signatures[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Signatures fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Signatures.class, obj);
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
    public static final Parcelable.Creator<Signatures> CREATOR = new Parcelable.Creator<Signatures>() {

        @Override
        public Signatures createFromParcel(Parcel source) {
            try {
                return Signatures.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Signatures[] newArray(int size) {
            return new Signatures[size];
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

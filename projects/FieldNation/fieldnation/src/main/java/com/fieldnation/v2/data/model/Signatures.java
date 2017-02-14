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

public class Signatures implements Parcelable {
    private static final String TAG = "Signatures";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Signature[] _results;

    public Signatures() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Signatures metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Signature[] results) {
        _results = results;
    }

    public Signature[] getResults() {
        return _results;
    }

    public Signatures results(Signature[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Signatures signatures) {
        try {
            return Serializer.serializeObject(signatures);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

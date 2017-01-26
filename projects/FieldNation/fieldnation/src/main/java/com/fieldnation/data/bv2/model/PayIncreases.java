package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class PayIncreases implements Parcelable {
    private static final String TAG = "PayIncreases";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private PayIncrease[] _results;

    public PayIncreases() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public PayIncreases metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(PayIncrease[] results) {
        _results = results;
    }

    public PayIncrease[] getResults() {
        return _results;
    }

    public PayIncreases results(PayIncrease[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayIncreases fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayIncreases.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayIncreases payIncreases) {
        try {
            return Serializer.serializeObject(payIncreases);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayIncreases> CREATOR = new Parcelable.Creator<PayIncreases>() {

        @Override
        public PayIncreases createFromParcel(Parcel source) {
            try {
                return PayIncreases.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayIncreases[] newArray(int size) {
            return new PayIncreases[size];
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

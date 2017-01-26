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

public class Expenses implements Parcelable {
    private static final String TAG = "Expenses";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Expense[] _results;

    public Expenses() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Expenses metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Expense[] results) {
        _results = results;
    }

    public Expense[] getResults() {
        return _results;
    }

    public Expenses results(Expense[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Expenses fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Expenses.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Expenses expenses) {
        try {
            return Serializer.serializeObject(expenses);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Expenses> CREATOR = new Parcelable.Creator<Expenses>() {

        @Override
        public Expenses createFromParcel(Parcel source) {
            try {
                return Expenses.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Expenses[] newArray(int size) {
            return new Expenses[size];
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

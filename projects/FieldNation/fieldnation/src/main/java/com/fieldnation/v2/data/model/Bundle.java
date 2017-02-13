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

public class Bundle implements Parcelable {
    private static final String TAG = "Bundle";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private WorkOrder[] _results;

    public Bundle() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Bundle id(Integer id) {
        _id = id;
        return this;
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Bundle metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(WorkOrder[] results) {
        _results = results;
    }

    public WorkOrder[] getResults() {
        return _results;
    }

    public Bundle results(WorkOrder[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Bundle[] fromJsonArray(JsonArray array) {
        Bundle[] list = new Bundle[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Bundle fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Bundle.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Bundle bundle) {
        try {
            return Serializer.serializeObject(bundle);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Bundle> CREATOR = new Parcelable.Creator<Bundle>() {

        @Override
        public Bundle createFromParcel(Parcel source) {
            try {
                return Bundle.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Bundle[] newArray(int size) {
            return new Bundle[size];
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

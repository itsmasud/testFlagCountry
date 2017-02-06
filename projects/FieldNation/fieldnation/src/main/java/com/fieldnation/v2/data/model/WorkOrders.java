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

public class WorkOrders implements Parcelable {
    private static final String TAG = "WorkOrders";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private WorkOrder[] _results;

    public WorkOrders() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public WorkOrders metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(WorkOrder[] results) {
        _results = results;
    }

    public WorkOrder[] getResults() {
        return _results;
    }

    public WorkOrders results(WorkOrder[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static WorkOrders[] fromJsonArray(JsonArray array) {
        WorkOrders[] list = new WorkOrders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(WorkOrders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrders workOrders) {
        try {
            return Serializer.serializeObject(workOrders);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrders> CREATOR = new Parcelable.Creator<WorkOrders>() {

        @Override
        public WorkOrders createFromParcel(Parcel source) {
            try {
                return WorkOrders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrders[] newArray(int size) {
            return new WorkOrders[size];
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

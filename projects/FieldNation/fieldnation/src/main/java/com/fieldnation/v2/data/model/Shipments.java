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

public class Shipments implements Parcelable {
    private static final String TAG = "Shipments";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Shipment[] _results;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    public Shipments() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Shipments metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(Shipment[] results) {
        _results = results;
    }

    public Shipment[] getResults() {
        return _results;
    }

    public Shipments results(Shipment[] results) {
        _results = results;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Shipments actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Shipments[] fromJsonArray(JsonArray array) {
        Shipments[] list = new Shipments[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Shipments fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Shipments.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Shipments shipments) {
        try {
            return Serializer.serializeObject(shipments);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Shipments> CREATOR = new Parcelable.Creator<Shipments>() {

        @Override
        public Shipments createFromParcel(Parcel source) {
            try {
                return Shipments.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Shipments[] newArray(int size) {
            return new Shipments[size];
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

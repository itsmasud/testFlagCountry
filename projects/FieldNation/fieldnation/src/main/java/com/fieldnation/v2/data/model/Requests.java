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

public class Requests implements Parcelable {
    private static final String TAG = "Requests";

    @Json(name = "actions")
    private ActionsEnum _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "open_request")
    private Request _openRequest;

    @Json(name = "results")
    private TimeLog[] _results;

    public Requests() {
    }

    public void setActions(ActionsEnum actions) {
        _actions = actions;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public Requests actions(ActionsEnum actions) {
        _actions = actions;
        return this;
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Requests metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setOpenRequest(Request openRequest) {
        _openRequest = openRequest;
    }

    public Request getOpenRequest() {
        return _openRequest;
    }

    public Requests openRequest(Request openRequest) {
        _openRequest = openRequest;
        return this;
    }

    public void setResults(TimeLog[] results) {
        _results = results;
    }

    public TimeLog[] getResults() {
        return _results;
    }

    public Requests results(TimeLog[] results) {
        _results = results;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Requests[] fromJsonArray(JsonArray array) {
        Requests[] list = new Requests[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Requests fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Requests.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Requests requests) {
        try {
            return Serializer.serializeObject(requests);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Requests> CREATOR = new Parcelable.Creator<Requests>() {

        @Override
        public Requests createFromParcel(Parcel source) {
            try {
                return Requests.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Requests[] newArray(int size) {
            return new Requests[size];
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

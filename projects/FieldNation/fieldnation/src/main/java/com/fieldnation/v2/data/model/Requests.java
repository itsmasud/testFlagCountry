package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

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
    private Request[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Requests() {
    }

    public void setActions(ActionsEnum actions) throws ParseException {
        _actions = actions;
        SOURCE.put("actions", actions.toString());
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public Requests actions(ActionsEnum actions) throws ParseException {
        _actions = actions;
        SOURCE.put("actions", actions.toString());
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Requests metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setOpenRequest(Request openRequest) throws ParseException {
        _openRequest = openRequest;
        SOURCE.put("open_request", openRequest.getJson());
    }

    public Request getOpenRequest() {
        return _openRequest;
    }

    public Requests openRequest(Request openRequest) throws ParseException {
        _openRequest = openRequest;
        SOURCE.put("open_request", openRequest.getJson());
        return this;
    }

    public void setResults(Request[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Request.toJsonArray(results));
    }

    public Request[] getResults() {
        return _results;
    }

    public Requests results(Request[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Request.toJsonArray(results), true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add"),
        @Json(name = "counter_offer")
        COUNTER_OFFER("counter_offer");

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
    public static JsonArray toJsonArray(Requests[] array) {
        JsonArray list = new JsonArray();
        for (Requests item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    public Request getCounterOffer() {
        if (getResults() == null || getResults().length == 0)
            return null;

        for (Request request : getResults()) {
            if (request.getActive() && request.getCounter())
                return request;
        }

        return null;
    }
}

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

public class Shipments implements Parcelable {
    private static final String TAG = "Shipments";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Shipment[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Shipments() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Shipments actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Shipments metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Shipment[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Shipment.toJsonArray(results));
    }

    public Shipment[] getResults() {
        return _results;
    }

    public Shipments results(Shipment[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Shipment.toJsonArray(results), true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "unknown")
        UNKNOWN("unknown");

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
    public static JsonArray toJsonArray(Shipments[] array) {
        JsonArray list = new JsonArray();
        for (Shipments item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Shipments {
    private static final String TAG = "Shipments";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Shipment[] _results;

    @Json(name = "actions")
    private String[] _actions;

    public Shipments() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Shipment[] getResults() {
        return _results;
    }

    public String[] getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}

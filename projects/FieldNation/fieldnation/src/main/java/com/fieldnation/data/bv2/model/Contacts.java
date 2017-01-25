package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Contacts {
    private static final String TAG = "Contacts";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "results")
    private Contact[] _results;

    @Json(name = "actions")
    private String[] _actions;

    public Contacts() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Contact[] getResults() {
        return _results;
    }

    public String[] getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Contacts fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contacts.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Contacts contacts) {
        try {
            return Serializer.serializeObject(contacts);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

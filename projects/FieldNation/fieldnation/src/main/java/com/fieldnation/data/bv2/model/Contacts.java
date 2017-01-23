package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Contacts {
    private static final String TAG = "Contacts";

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "results")
    private Contact[] results;

    @Json(name = "actions")
    private String[] actions;

    public Contacts() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Contact[] getResults() {
        return results;
    }

    public String[] getActions() {
        return actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Contacts fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contacts.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
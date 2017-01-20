package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Cancellation {
    private static final String TAG = "Cancellation";

    @Json(name = "cancel_reason")
    private Integer cancelReason = null;

    @Json(name = "notes")
    private String notes = null;

    public Cancellation() {
    }

    public Integer getCancelReason() {
        return cancelReason;
    }

    public String getNotes() {
        return notes;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Cancellation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Cancellation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Cancellation cancellation) {
        try {
            return Serializer.serializeObject(cancellation);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


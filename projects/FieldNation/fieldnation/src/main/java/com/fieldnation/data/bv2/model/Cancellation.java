package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Cancellation {
    private static final String TAG = "Cancellation";

    @Json(name = "cancel_reason")
    private Integer _cancelReason;

    @Json(name = "notes")
    private String _notes;

    public Cancellation() {
    }

    public Integer getCancelReason() {
        return _cancelReason;
    }

    public String getNotes() {
        return _notes;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Cancellation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Cancellation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

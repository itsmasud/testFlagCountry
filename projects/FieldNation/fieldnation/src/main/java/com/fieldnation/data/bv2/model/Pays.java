package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Pays {
    private static final String TAG = "Pays";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "results")
    private Pay[] results;

    public Pays() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Pay[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Pays fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Pays.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Pays pays) {
        try {
            return Serializer.serializeObject(pays);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
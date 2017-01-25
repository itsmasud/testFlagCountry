package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Holds {
    private static final String TAG = "Holds";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Hold[] _results;

    public Holds() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Hold[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Holds fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Holds.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Holds holds) {
        try {
            return Serializer.serializeObject(holds);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

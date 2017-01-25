package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayIncreases {
    private static final String TAG = "PayIncreases";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private PayIncrease[] _results;

    public PayIncreases() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public PayIncrease[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayIncreases fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayIncreases.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayIncreases payIncreases) {
        try {
            return Serializer.serializeObject(payIncreases);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

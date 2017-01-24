package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifiersSum {
    private static final String TAG = "PayModifiersSum";

    @Json(name = "all")
    private Double all;

    @Json(name = "uncharged")
    private Double uncharged;

    @Json(name = "charged")
    private Double charged;

    public PayModifiersSum() {
    }

    public Double getAll() {
        return all;
    }

    public Double getUncharged() {
        return uncharged;
    }

    public Double getCharged() {
        return charged;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiersSum fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiersSum.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifiersSum payModifiersSum) {
        try {
            return Serializer.serializeObject(payModifiersSum);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

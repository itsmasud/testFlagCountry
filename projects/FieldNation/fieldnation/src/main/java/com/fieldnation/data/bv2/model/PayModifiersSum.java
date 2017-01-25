package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifiersSum {
    private static final String TAG = "PayModifiersSum";

    @Json(name = "all")
    private Double _all;

    @Json(name = "uncharged")
    private Double _uncharged;

    @Json(name = "charged")
    private Double _charged;

    public PayModifiersSum() {
    }

    public Double getAll() {
        return _all;
    }

    public Double getUncharged() {
        return _uncharged;
    }

    public Double getCharged() {
        return _charged;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiersSum fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiersSum.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

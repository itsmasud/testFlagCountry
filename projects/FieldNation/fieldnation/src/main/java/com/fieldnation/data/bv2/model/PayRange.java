package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayRange {
    private static final String TAG = "PayRange";

    @Json(name = "min")
    private Double min = null;

    @Json(name = "max")
    private Double max = null;

    public PayRange() {
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayRange fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayRange.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayRange payRange) {
        try {
            return Serializer.serializeObject(payRange);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayRange {
    private static final String TAG = "PayRange";

    @Json(name = "min")
    private Double _min;

    @Json(name = "max")
    private Double _max;

    public PayRange() {
    }

    public Double getMin() {
        return _min;
    }

    public Double getMax() {
        return _max;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayRange fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayRange.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

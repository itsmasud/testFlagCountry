package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayAdditional {
    private static final String TAG = "PayAdditional";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "units")
    private Double _units;

    public PayAdditional() {
    }

    public Double getAmount() {
        return _amount;
    }

    public Double getUnits() {
        return _units;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayAdditional fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayAdditional.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayAdditional payAdditional) {
        try {
            return Serializer.serializeObject(payAdditional);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

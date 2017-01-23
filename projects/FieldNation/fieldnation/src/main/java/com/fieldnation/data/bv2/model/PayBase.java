package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayBase {
    private static final String TAG = "PayBase";

    @Json(name = "units")
    private Double units = null;

    @Json(name = "amount")
    private Double amount = null;

    public PayBase() {
    }

    public Double getUnits() {
        return units;
    }

    public Double getAmount() {
        return amount;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayBase fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayBase.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayBase payBase) {
        try {
            return Serializer.serializeObject(payBase);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayFees {
    private static final String TAG = "PayFees";

    @Json(name = "cancellation")
    private Fee cancellation = null;

    @Json(name = "insurance")
    private Fee insurance = null;

    @Json(name = "flat")
    private Fee flat = null;

    @Json(name = "provider")
    private Fee provider = null;

    @Json(name = "buyer")
    private Fee buyer = null;

    public PayFees() {
    }

    public Fee getCancellation() {
        return cancellation;
    }

    public Fee getInsurance() {
        return insurance;
    }

    public Fee getFlat() {
        return flat;
    }

    public Fee getProvider() {
        return provider;
    }

    public Fee getBuyer() {
        return buyer;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayFees fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayFees.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayFees payFees) {
        try {
            return Serializer.serializeObject(payFees);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
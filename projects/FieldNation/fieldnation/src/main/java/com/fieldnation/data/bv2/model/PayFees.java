package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayFees {
    private static final String TAG = "PayFees";

    @Json(name = "insurance")
    private Fee _insurance;

    @Json(name = "cancellation")
    private Fee _cancellation;

    @Json(name = "provider")
    private Fee _provider;

    @Json(name = "flat")
    private Fee _flat;

    @Json(name = "buyer")
    private Fee _buyer;

    public PayFees() {
    }

    public Fee getInsurance() {
        return _insurance;
    }

    public Fee getCancellation() {
        return _cancellation;
    }

    public Fee getProvider() {
        return _provider;
    }

    public Fee getFlat() {
        return _flat;
    }

    public Fee getBuyer() {
        return _buyer;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayFees fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayFees.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

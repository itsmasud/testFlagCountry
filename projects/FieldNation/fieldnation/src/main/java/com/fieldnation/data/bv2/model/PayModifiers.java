package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifiers {
    private static final String TAG = "PayModifiers";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "sum")
    private PayModifiersSum _sum;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "results")
    private PayModifier[] _results;

    public PayModifiers() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public PayModifiersSum getSum() {
        return _sum;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public PayModifier[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiers fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiers.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifiers payModifiers) {
        try {
            return Serializer.serializeObject(payModifiers);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

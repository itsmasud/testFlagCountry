package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifiers {
    private static final String TAG = "PayModifiers";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "sum")
    private PayModifiersSum sum = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "results")
    private PayModifier[] results;

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public PayModifiers() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public PayModifiersSum getSum() {
        return sum;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public PayModifier[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiers fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiers.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifier {
    private static final String TAG = "PayModifier";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "charged")
    private Boolean charged = null;

    @Json(name = "amount")
    private Double amount = null;

    @Json(name = "modifier")
    private Double modifier = null;

    @Json(name = "calculation")
    private CalculationEnum calculation = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "description")
    private String description = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    public enum CalculationEnum {
        @Json(name = "fixed")
        FIXED("fixed"),
        @Json(name = "percent")
        PERCENT("percent");

        private String value;

        CalculationEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "charge")
        CHARGE("charge"),
        @Json(name = "remove")
        REMOVE("remove");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public PayModifier() {
    }

    public Integer getId() {
        return id;
    }

    public Boolean getCharged() {
        return charged;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getModifier() {
        return modifier;
    }

    public CalculationEnum getCalculation() {
        return calculation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifier payModifier) {
        try {
            return Serializer.serializeObject(payModifier);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
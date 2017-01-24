package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifier {
    private static final String TAG = "PayModifier";

    @Json(name = "calculation")
    private CalculationEnum calculation;

    @Json(name = "amount")
    private Double amount;

    @Json(name = "modifier")
    private Double modifier;

    @Json(name = "name")
    private String name;

    @Json(name = "description")
    private String description;

    @Json(name = "id")
    private Integer id;

    @Json(name = "charged")
    private Boolean charged;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    public PayModifier() {
    }

    public CalculationEnum getCalculation() {
        return calculation;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getCharged() {
        return charged;
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

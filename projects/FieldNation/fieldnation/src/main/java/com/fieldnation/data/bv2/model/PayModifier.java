package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayModifier {
    private static final String TAG = "PayModifier";

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "modifier")
    private Double _modifier;

    @Json(name = "name")
    private String _name;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "charged")
    private Boolean _charged;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    public PayModifier() {
    }

    public CalculationEnum getCalculation() {
        return _calculation;
    }

    public Double getAmount() {
        return _amount;
    }

    public Double getModifier() {
        return _modifier;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public Integer getId() {
        return _id;
    }

    public Boolean getCharged() {
        return _charged;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

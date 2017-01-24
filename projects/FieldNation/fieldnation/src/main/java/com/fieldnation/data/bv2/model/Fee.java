package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Fee {
    private static final String TAG = "Fee";

    @Json(name = "calculation")
    private CalculationEnum calculation;

    @Json(name = "amount")
    private Double amount;

    @Json(name = "modifier")
    private Double modifier;

    @Json(name = "id")
    private Integer id;

    @Json(name = "charged")
    private Boolean charged;

    public Fee() {
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

    public Integer getId() {
        return id;
    }

    public Boolean getCharged() {
        return charged;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Fee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Fee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Fee fee) {
        try {
            return Serializer.serializeObject(fee);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

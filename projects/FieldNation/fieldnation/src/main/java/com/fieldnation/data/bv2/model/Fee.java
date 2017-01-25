package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Fee {
    private static final String TAG = "Fee";

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "modifier")
    private Double _modifier;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "charged")
    private Boolean _charged;

    public Fee() {
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

    public Integer getId() {
        return _id;
    }

    public Boolean getCharged() {
        return _charged;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Fee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Fee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

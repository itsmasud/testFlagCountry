package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Fee {
    private static final String TAG = "Fee";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "amount")
    private Double amount = null;

    @Json(name = "calculation")
    private CalculationEnum calculation = null;

    @Json(name = "modifier")
    private Double modifier = null;

    @Json(name = "charged")
    private Boolean charged = null;

    public enum CalculationEnum {
        PERCENT("percent"),
        FIXED("fixed");

        private String value;

        CalculationEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static CalculationEnum fromValue(String value) {
            CalculationEnum[] values = values();
            for (CalculationEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public Fee() {
    }

    public Integer getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public CalculationEnum getCalculation() {
        return calculation;
    }

    public Double getModifier() {
        return modifier;
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


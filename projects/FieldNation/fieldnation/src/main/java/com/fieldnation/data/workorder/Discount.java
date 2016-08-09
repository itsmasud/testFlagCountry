package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class Discount {
    private static final String TAG = "Discount";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "description")
    private String _description;
    @Json(name = "discountId")
    private Integer _discountId;

    public Discount() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getDescription() {
        return _description;
    }

    public Integer getDiscountId() {
        return _discountId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Discount discount) {
        try {
            return Serializer.serializeObject(discount);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Discount fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Discount.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class Fee {
    private static final String TAG = "Fee";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "chargeType")
    private String _chargeType;
    @Json(name = "name")
    private String _name;

    public Fee() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getChargeType() {
        return _chargeType;
    }

    public String getName() {
        return _name;
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

    public static Fee fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Fee.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

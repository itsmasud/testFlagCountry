package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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

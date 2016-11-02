package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Bonus {
    private static final String TAG = "Bonus";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "name")
    private String _name;

    public Bonus() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getName() {
        return _name;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Bonus bonus) {
        try {
            return Serializer.serializeObject(bonus);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Bonus fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Bonus.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class Bonuses {
    private static final String TAG = "Bonuses";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "name")
    private String _name;

    public Bonuses() {
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

    public static JsonObject toJson(Bonuses bonuses) {
        try {
            return Serializer.serializeObject(bonuses);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Bonuses fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Bonuses.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

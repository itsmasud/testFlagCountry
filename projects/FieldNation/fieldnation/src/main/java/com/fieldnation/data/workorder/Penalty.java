package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Penalty {
    private static final String TAG = "Penalty";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "name")
    private String _name;

    public Penalty() {
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

    public static JsonObject toJson(Penalty penalty) {
        try {
            return Serializer.serializeObject(penalty);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Penalty fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Penalty.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
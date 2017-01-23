package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Holds {
    private static final String TAG = "Holds";

    @Json(name = "holds")
    private Hold[] holds;

    public Holds() {
    }

    public Hold[] getHolds() {
        return holds;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Holds fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Holds.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Holds holds) {
        try {
            return Serializer.serializeObject(holds);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


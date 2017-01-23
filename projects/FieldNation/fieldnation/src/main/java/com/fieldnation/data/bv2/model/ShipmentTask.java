package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ShipmentTask {
    private static final String TAG = "ShipmentTask";

    @Json(name = "id")
    private Double id = null;

    public ShipmentTask() {
    }

    public Double getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ShipmentTask fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ShipmentTask.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ShipmentTask shipmentTask) {
        try {
            return Serializer.serializeObject(shipmentTask);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
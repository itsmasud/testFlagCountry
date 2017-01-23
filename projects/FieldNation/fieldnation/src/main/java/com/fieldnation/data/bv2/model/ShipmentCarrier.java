package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ShipmentCarrier {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "name")
    private String name = null;

    @Json(name = "other")
    private String other = null;

    @Json(name = "tracking")
    private String tracking = null;

    @Json(name = "arrival")
    private String arrival = null;

    @Json(name = "arrived")
    private String arrived = null;

    public ShipmentCarrier() {
    }

    public String getName() {
        return name;
    }

    public String getOther() {
        return other;
    }

    public String getTracking() {
        return tracking;
    }

    public String getArrival() {
        return arrival;
    }

    public String getArrived() {
        return arrived;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ShipmentCarrier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ShipmentCarrier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ShipmentCarrier shipmentCarrier) {
        try {
            return Serializer.serializeObject(shipmentCarrier);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ShipmentCarrier {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "arrived")
    private Date arrived;

    @Json(name = "other")
    private String other;

    @Json(name = "arrival")
    private Date arrival;

    @Json(name = "name")
    private String name;

    @Json(name = "tracking")
    private String tracking;

    public ShipmentCarrier() {
    }

    public Date getArrived() {
        return arrived;
    }

    public String getOther() {
        return other;
    }

    public Date getArrival() {
        return arrival;
    }

    public String getName() {
        return name;
    }

    public String getTracking() {
        return tracking;
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

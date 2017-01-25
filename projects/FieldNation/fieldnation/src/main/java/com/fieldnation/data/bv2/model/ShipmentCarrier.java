package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ShipmentCarrier {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "arrived")
    private Date _arrived;

    @Json(name = "other")
    private String _other;

    @Json(name = "arrival")
    private Date _arrival;

    @Json(name = "name")
    private String _name;

    @Json(name = "tracking")
    private String _tracking;

    public ShipmentCarrier() {
    }

    public Date getArrived() {
        return _arrived;
    }

    public String getOther() {
        return _other;
    }

    public Date getArrival() {
        return _arrival;
    }

    public String getName() {
        return _name;
    }

    public String getTracking() {
        return _tracking;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ShipmentCarrier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ShipmentCarrier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

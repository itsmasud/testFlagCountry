package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Shipment {
    private static final String TAG = "Shipment";

    @Json(name = "carrier")
    private ShipmentCarrier _carrier;

    @Json(name = "task")
    private ShipmentTask _task;

    @Json(name = "created")
    private Date _created;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "user")
    private User _user;

    @Json(name = "direction")
    private DirectionEnum _direction;

    @Json(name = "status")
    private StatusEnum _status;

    public Shipment() {
    }

    public ShipmentCarrier getCarrier() {
        return _carrier;
    }

    public ShipmentTask getTask() {
        return _task;
    }

    public Date getCreated() {
        return _created;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public User getUser() {
        return _user;
    }

    public DirectionEnum getDirection() {
        return _direction;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Shipment fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Shipment.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Shipment shipment) {
        try {
            return Serializer.serializeObject(shipment);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Shipment {
    private static final String TAG = "Shipment";

    @Json(name = "carrier")
    private ShipmentCarrier carrier;

    @Json(name = "task")
    private ShipmentTask task;

    @Json(name = "created")
    private Date created;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    @Json(name = "user")
    private User user;

    @Json(name = "direction")
    private DirectionEnum direction;

    @Json(name = "status")
    private StatusEnum status;

    public Shipment() {
    }

    public ShipmentCarrier getCarrier() {
        return carrier;
    }

    public ShipmentTask getTask() {
        return task;
    }

    public Date getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public StatusEnum getStatus() {
        return status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Shipment fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Shipment.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

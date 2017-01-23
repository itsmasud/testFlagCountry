package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Shipment {
    public static final String TAG = "Shipment";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "user")
    private User user = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "direction")
    private DirectionEnum direction = null;

    @Json(name = "carrier")
    private ShipmentCarrier carrier = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "task")
    private ShipmentTask task = null;

    @Json(name = "status")
    private StatusEnum status = null;

    public enum DirectionEnum {
        @Json(name = "to site")
        TO_SITE("to site"),
        @Json(name = "from site")
        FROM_SITE("from site");

        private String value;

        DirectionEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum StatusEnum {
        @Json(name = "Arrived")
        ARRIVED("Arrived"),
        @Json(name = "En Route")
        EN_ROUTE("En Route"),
        @Json(name = "Error")
        ERROR("Error"),
        @Json(name = "Lost")
        LOST("Lost"),
        @Json(name = "New")
        NEW("New");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public Shipment() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public ShipmentCarrier getCarrier() {
        return carrier;
    }

    public String getCreated() {
        return created;
    }

    public ShipmentTask getTask() {
        return task;
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
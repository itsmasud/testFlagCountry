package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class ShipmentTracking {
    private static final String TAG = "ShipmentTracking";

    @Json(name = "carrier")
    private String _carrier;
    @Json(name = "carrierOther")
    private String _carrierOther;
    @Json(name = "currentStatus")
    private String _currentStatus;
    @Json(name = "dateArrived")
    private Object _dateArrived;
    @Json(name = "dateCreated")
    private Integer _dateCreated;
    @Json(name = "dateOfFirstTrackedActivity")
    private Object _dateOfFirstTrackedActivity;
    @Json(name = "dateProjectedToArrive")
    private Object _dateProjectedToArrive;
    @Json(name = "deleted")
    private Integer _deleted;
    @Json(name = "direction")
    private String _direction;
    @Json(name = "name")
    private String _name;
    @Json(name = "trackingId")
    private String _trackingId;
    @Json(name = "userId")
    private Long _userId;
    @Json(name = "workorderId")
    private Integer _workorderId;
    @Json(name = "workorderShipmentId")
    private Integer _workorderShipmentId;

    public ShipmentTracking() {
    }

    public String getCarrier() {
        return _carrier;
    }

    public String getCarrierOther() {
        return _carrierOther;
    }

    public String getCurrentStatus() {
        return _currentStatus;
    }

    public Object getDateArrived() {
        return _dateArrived;
    }

    public Integer getDateCreated() {
        return _dateCreated;
    }

    public Object getDateOfFirstTrackedActivity() {
        return _dateOfFirstTrackedActivity;
    }

    public Object getDateProjectedToArrive() {
        return _dateProjectedToArrive;
    }

    public Integer getDeleted() {
        return _deleted;
    }

    public String getDirection() {
        return _direction;
    }

    public String getName() {
        return _name;
    }

    public String getTrackingId() {
        return _trackingId;
    }

    public Long getUserId() {
        return _userId;
    }

    public Integer getWorkorderId() {
        return _workorderId;
    }

    public Integer getWorkorderShipmentId() {
        return _workorderShipmentId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ShipmentTracking shipmentTracking) {
        try {
            return Serializer.serializeObject(shipmentTracking);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ShipmentTracking fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(ShipmentTracking.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

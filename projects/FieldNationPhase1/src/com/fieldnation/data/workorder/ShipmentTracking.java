package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ShipmentTracking {
	@Json(name = "name")
	private String _name;
	@Json(name = "deleted")
	private Integer _deleted;
	@Json(name="dateProjectedToArrive")
	private Object _dateProjectedToArrive;
	@Json(name="workorderShipmentId")
	private Integer _workorderShipmentId;
	@Json(name="userId")
	private Integer _userId;
	@Json(name = "carrier")
	private String _carrier;
	@Json(name="carrierOther")
	private String _carrierOther;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name = "direction")
	private String _direction;
	@Json(name="dateOfFirstTrackedActivity")
	private Object _dateOfFirstTrackedActivity;
	@Json(name="dateCreated")
	private String _dateCreated;
	@Json(name="trackingId")
	private String _trackingId;
	@Json(name="dateArrived")
	private Object _dateArrived;
	@Json(name="currentStatus")
	private String _currentStatus;

	public ShipmentTracking() {
	}

	public String getName() {
		return _name;
	}

	public Integer getDeleted() {
		return _deleted;
	}

	public Object getDateProjectedToArrive(){
		return _dateProjectedToArrive;
	}

	public Integer getWorkorderShipmentId(){
		return _workorderShipmentId;
	}

	public Integer getUserId(){
		return _userId;
	}

	public String getCarrier(){
		return _carrier;
	}

	public String getCarrierOther() {
		return _carrierOther;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public String getDirection(){
		return _direction;
	}

	public Object getDateOfFirstTrackedActivity(){
		return _dateOfFirstTrackedActivity;
	}

	public String getDateCreated(){
		return _dateCreated;
	}

	public String getTrackingId(){
		return _trackingId;
	}

	public Object getDateArrived(){
		return _dateArrived;
	}

	public String getCurrentStatus(){
		return _currentStatus;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(ShipmentTracking shipmentTracking) {
		try {
			return Serializer.serializeObject(shipmentTracking);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ShipmentTracking fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ShipmentTracking.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

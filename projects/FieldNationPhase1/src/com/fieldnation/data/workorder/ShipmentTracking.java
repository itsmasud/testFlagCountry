package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ShipmentTracking{
	@Json(name="name")
	private String _name;
	@Json(name="workorder_shipment_id")
	private Integer _workorderShipmentId;
	@Json(name="tracking_id")
	private Integer _trackingId;
	@Json(name="deleted")
	private Integer _deleted;
	@Json(name="workorder_id")
	private Integer _workorderId;
	@Json(name="date_projected_to_arrive")
	private Integer _dateProjectedToArrive;
	@Json(name="current_status")
	private String _currentStatus;
	@Json(name="date_of_first_tracked_activity")
	private Integer _dateOfFirstTrackedActivity;
	@Json(name="carrier_other")
	private String _carrierOther;
	@Json(name="carrier")
	private String _carrier;
	@Json(name="date_created")
	private Integer _dateCreated;
	@Json(name="direction")
	private String _direction;
	@Json(name="date_arrived")
	private Integer _dateArrived;
	@Json(name="user_id")
	private Integer _userId;

	public ShipmentTracking(){
	}
	public String getName(){
		return _name;
	}

	public Integer getWorkorderShipmentId(){
		return _workorderShipmentId;
	}

	public Integer getTrackingId(){
		return _trackingId;
	}

	public Integer getDeleted(){
		return _deleted;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public Integer getDateProjectedToArrive(){
		return _dateProjectedToArrive;
	}

	public String getCurrentStatus(){
		return _currentStatus;
	}

	public Integer getDateOfFirstTrackedActivity(){
		return _dateOfFirstTrackedActivity;
	}

	public String getCarrierOther(){
		return _carrierOther;
	}

	public String getCarrier(){
		return _carrier;
	}

	public Integer getDateCreated(){
		return _dateCreated;
	}

	public String getDirection(){
		return _direction;
	}

	public Integer getDateArrived(){
		return _dateArrived;
	}

	public Integer getUserId(){
		return _userId;
	}

	public JsonObject toJson(){
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

package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "workorder_id")
	private int _workorderId;
	@Json(name = "location")
	private Location _location;
	@Json(name = "statusId")
	private int _statusId;
	@Json(name = "identifier")
	private String _identifier;
	@Json(name = "distance")
	private double _distance;
	@Json(name = "providersPhoto")
	private ProvidersPhoto[] _providersPhoto;
	@Json(name = "bundleId")
	private String _bundleId;
	@Json(name = "industry")
	private String _industry;
	@Json(name = "label")
	private Label[] _label;
	@Json(name = "skillsets")
	private Skillsets[] _skillsets;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "standard_instructions")
	private String _standardInstructions;
	@Json(name = "scheduledTimeStart")
	private int _scheduledTimeStart;
	@Json(name = "scheduledTimeEnd")
	private int _scheduledTimeEnd;
	@Json(name = "status")
	private String _status;
	@Json(name = "declinedWo")
	private int _declinedWo;
	@Json(name = "schedule")
	private Schedule _schedule;
	@Json(name = "isRemoteWork")
	private int _isRemoteWork;
	@Json(name = "provider")
	private Provider[] _provider;
	@Json(name = "providersMore")
	private ProvidersMore[] _providersMore;
	@Json(name = "typeOfWork")
	private String _typeOfWork;
	@Json(name = "title")
	private String _title;

	public Workorder() {
	}

	public int getWorkorderId() {
		return _workorderId;
	}

	public Location getLocation() {
		return _location;
	}

	public int getStatusId() {
		return _statusId;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public double getDistance() {
		return _distance;
	}

	public ProvidersPhoto[] getProvidersPhoto() {
		return _providersPhoto;
	}

	public String getBundleId() {
		return _bundleId;
	}

	public String getIndustry() {
		return _industry;
	}

	public Label[] getLabel() {
		return _label;
	}

	public Skillsets[] getSkillsets() {
		return _skillsets;
	}

	public Pay getPay() {
		return _pay;
	}

	public String getStandardInstructions() {
		return _standardInstructions;
	}

	public int getScheduledTimeStart() {
		return _scheduledTimeStart;
	}

	public int getScheduledTimeEnd() {
		return _scheduledTimeEnd;
	}

	public String getStatus() {
		return _status;
	}

	public int getDeclinedWo() {
		return _declinedWo;
	}

	public Schedule getSchedule() {
		return _schedule;
	}

	public int getIsRemoteWork() {
		return _isRemoteWork;
	}

	public Provider[] getProvider() {
		return _provider;
	}

	public ProvidersMore[] getProvidersMore() {
		return _providersMore;
	}

	public String getTypeOfWork() {
		return _typeOfWork;
	}

	public String getTitle() {
		return _title;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder workorder) {
		try {
			return Serializer.serializeObject(workorder);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

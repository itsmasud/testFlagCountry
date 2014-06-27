package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.annotations.ToJsonFloat;
import com.fieldnation.json.annotations.ToJsonInt;
import com.fieldnation.json.annotations.ToJsonObject;

public class Workorder {
	@ToJsonInt(name = "workorder_id")
	private int _workorderId;
	@ToJsonObject(name = "location")
	private Location _location;
	@ToJsonInt(name = "statusId")
	private int _statusId;
	@ToJsonFloat(name = "distance")
	private float _distance;
	@ToJsonObject(name = "providersPhoto")
	private ProviderPhoto _providersPhoto;
	
	private Label[] _labels;
	private Pay _pay;
	private Provider[] _provider;
	private String _title;

	/**
	 * never call this, call fromJson instead
	 */
	public Workorder() {
	}

	public static Workorder fromJson(JsonObject json) {
		return null;
	}
}

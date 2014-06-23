package com.fieldnation.workorder;

import com.fieldnation.json.JsonObject;

public class Workorder {
	private int _workorderId;
	private Location _location;
	private int _statusId;
	private float _distance;
	private ProviderPhoto _providersPhoto;
	private int _bundleId = -1;
	private Label[] _labels;
	private Pay _pay;
	private Provider[] _provider;
	private String _title;
	
	public Workorder(JsonObject json) {

	}
}

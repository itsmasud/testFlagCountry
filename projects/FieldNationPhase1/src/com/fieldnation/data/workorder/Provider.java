package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Provider {
	@Json(name = "user_id")
	private long _userId = -1;
	@Json(name = "longitude")
	private double _longitude = -1;
	@Json(name = "latitude")
	private double _latitude = -1;
	@Json(name = "distance")
	private double _distance = 0;

	public Provider() {

	}
}

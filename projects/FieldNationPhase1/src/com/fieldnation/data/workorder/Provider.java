package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Provider {
	@Json(name = "user_id")
	private int _userId;
	@Json(name = "longitude")
	private double _longitude;
	@Json(name = "latitude")
	private double _latitude;
	@Json(name = "distance")
	private double _distance;

	public Provider() {
	}

	public int getUserId() {
		return _userId;
	}

	public double getLongitude() {
		return _longitude;
	}

	public double getLatitude() {
		return _latitude;
	}

	public double getDistance() {
		return _distance;
	}

}

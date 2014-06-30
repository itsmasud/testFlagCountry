package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Location{
	@Json(name="zip")
	private int _zip;
	@Json(name="state")
	private String _state;
	@Json(name="country")
	private String _country;
	@Json(name="city")
	private String _city;

	public Location(){
	}
	public int getZip(){
		return _zip;
	}

	public String getState(){
		return _state;
	}

	public String getCountry(){
		return _country;
	}

	public String getCity(){
		return _city;
	}

}

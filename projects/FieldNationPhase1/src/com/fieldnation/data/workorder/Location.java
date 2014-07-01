package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Location{
	@Json(name="distance")
	private double _distance;
	@Json(name="state")
	private String _state;
	@Json(name="address2")
	private String _address2;
	@Json(name="contact_phone_ext")
	private String _contactPhoneExt;
	@Json(name="address1")
	private String _address1;
	@Json(name="zip")
	private int _zip;
	@Json(name="country")
	private String _country;
	@Json(name="city")
	private String _city;
	@Json(name="contact_name")
	private String _contactName;
	@Json(name="contact_phone")
	private double _contactPhone;
	@Json(name="contact_email")
	private String _contactEmail;

	public Location(){
	}
	public double getDistance(){
		return _distance;
	}

	public String getState(){
		return _state;
	}

	public String getAddress2(){
		return _address2;
	}

	public String getContactPhoneExt(){
		return _contactPhoneExt;
	}

	public String getAddress1(){
		return _address1;
	}

	public int getZip(){
		return _zip;
	}

	public String getCountry(){
		return _country;
	}

	public String getCity(){
		return _city;
	}

	public String getContactName(){
		return _contactName;
	}

	public double getContactPhone(){
		return _contactPhone;
	}

	public String getContactEmail(){
		return _contactEmail;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Location location) {
		try {
			return Serializer.serializeObject(location);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Location fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Location.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

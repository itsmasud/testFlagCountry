package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Provider{
	@Json(name="longitude")
	private double _longitude;
	@Json(name="distance")
	private double _distance;
	@Json(name="state")
	private String _state;
	@Json(name="photo_url")
	private String _photoUrl;
	@Json(name="city")
	private String _city;
	@Json(name="firstname")
	private String _firstname;
	@Json(name="lastname")
	private String _lastname;
	@Json(name="latitude")
	private double _latitude;
	@Json(name="photo_thumb_url")
	private String _photoThumbUrl;
	@Json(name="user_id")
	private int _userId;

	public Provider(){
	}
	public double getLongitude(){
		return _longitude;
	}

	public double getDistance(){
		return _distance;
	}

	public String getState(){
		return _state;
	}

	public String getPhotoUrl(){
		return _photoUrl;
	}

	public String getCity(){
		return _city;
	}

	public String getFirstname(){
		return _firstname;
	}

	public String getLastname(){
		return _lastname;
	}

	public double getLatitude(){
		return _latitude;
	}

	public String getPhotoThumbUrl(){
		return _photoThumbUrl;
	}

	public int getUserId(){
		return _userId;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Provider provider) {
		try {
			return Serializer.serializeObject(provider);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Provider fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Provider.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

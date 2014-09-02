package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Provider {
	@Json(name = "firstName")
	private String _firstName;
	@Json(name = "cell")
	private Double _cell;
	@Json(name = "photo")
	private Photo _photo;
	@Json(name = "userId")
	private Integer _userId;
	@Json(name = "distance")
	private Double _distance;
	@Json(name = "username")
	private String _username;
	@Json(name = "state")
	private String _state;
	@Json(name = "ext")
	private String _ext;
	@Json(name = "phone")
	private Double _phone;
	@Json(name = "lastname")
	private String _lastname;
	@Json(name = "photoUrl")
	private String _photoUrl;
	@Json(name = "longitude")
	private Double _longitude;
	@Json(name = "lastName")
	private String _lastName;
	@Json(name = "latitude")
	private Double _latitude;
	@Json(name = "email")
	private String _email;
	@Json(name = "city")
	private String _city;
	@Json(name = "firstname")
	private String _firstname;
	@Json(name = "zip")
	private Integer _zip;
	@Json(name = "photoThumbUrl")
	private String _photoThumbUrl;

	public Provider() {
	}

	public String getFirstName() {
		return _firstName;
	}

	public Double getCell() {
		return _cell;
	}

	public Photo getPhoto() {
		return _photo;
	}

	public Integer getUserId() {
		return _userId;
	}

	public Double getDistance() {
		return _distance;
	}

	public String getUsername() {
		return _username;
	}

	public String getState() {
		return _state;
	}

	public String getExt() {
		return _ext;
	}

	public Double getPhone() {
		return _phone;
	}

	public String getLastname() {
		return _lastname;
	}

	public String getPhotoUrl() {
		return _photoUrl;
	}

	public Double getLongitude() {
		return _longitude;
	}

	public String getLastName() {
		return _lastName;
	}

	public Double getLatitude() {
		return _latitude;
	}

	public String getEmail() {
		return _email;
	}

	public String getCity() {
		return _city;
	}

	public String getFirstname() {
		return _firstname;
	}

	public Integer getZip() {
		return _zip;
	}

	public String getPhotoThumbUrl() {
		return _photoThumbUrl;
	}

	public JsonObject toJson() {
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

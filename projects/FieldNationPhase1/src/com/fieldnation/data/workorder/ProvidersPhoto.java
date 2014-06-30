package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ProvidersPhoto {
	@Json(name = "user_id")
	private int _userId;
	@Json(name = "photo_url")
	private String _photoUrl;
	@Json(name = "lastname")
	private String _lastname;
	@Json(name = "state")
	private String _state;
	@Json(name = "firstname")
	private String _firstname;
	@Json(name = "city")
	private String _city;

	public ProvidersPhoto() {
	}

	public int getUserId() {
		return _userId;
	}

	public String getPhotoUrl() {
		return _photoUrl;
	}

	public String getLastname() {
		return _lastname;
	}

	public String getState() {
		return _state;
	}

	public String getFirstname() {
		return _firstname;
	}

	public String getCity() {
		return _city;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(ProvidersPhoto providersPhoto) {
		try {
			return Serializer.serializeObject(providersPhoto);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ProvidersPhoto fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ProvidersPhoto.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

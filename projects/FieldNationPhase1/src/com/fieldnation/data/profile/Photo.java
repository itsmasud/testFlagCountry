package com.fieldnation.data.profile;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Photo{
	@Json(name="thumb")
	private String _thumb;
	@Json(name="large")
	private String _large;

	public Photo(){
	}
	public String getThumb(){
		return _thumb;
	}

	public String getLarge(){
		return _large;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Photo photo) {
		try {
			return Serializer.serializeObject(photo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Photo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Photo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

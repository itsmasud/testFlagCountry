package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Photo{
	@Json(name="image")
	private String _image;
	@Json(name="thumb")
	private String _thumb;

	public Photo(){
	}
	public String getImage(){
		return _image;
	}

	public String getThumb(){
		return _thumb;
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

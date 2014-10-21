package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomDisplayFields{
	@Json(name="label")
	private String _label;
	@Json(name="value")
	private String _value;

	public CustomDisplayFields(){
	}
	public String getLabel(){
		return _label;
	}

	public String getValue(){
		return _value;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(CustomDisplayFields customDisplayFields) {
		try {
			return Serializer.serializeObject(customDisplayFields);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CustomDisplayFields fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CustomDisplayFields.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

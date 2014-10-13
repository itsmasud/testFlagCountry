package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class PredefinedValues{
	@Json(name="0")
	private String _0;
	@Json(name="1")
	private String _1;

	public PredefinedValues(){
	}
	public String get0(){
		return _0;
	}

	public String get1(){
		return _1;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(PredefinedValues predefinedValues) {
		try {
			return Serializer.serializeObject(predefinedValues);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static PredefinedValues fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(PredefinedValues.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

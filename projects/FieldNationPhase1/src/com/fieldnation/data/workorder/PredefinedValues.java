package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class PredefinedValues{
	@Json(name="2")
	private Object _2;
	@Json(name="1")
	private String _1;
	@Json(name="0")
	private String _0;

	public PredefinedValues(){
	}
	public Object get2(){
		return _2;
	}

	public String get1(){
		return _1;
	}

	public String get0(){
		return _0;
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

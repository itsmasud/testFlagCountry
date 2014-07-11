package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class StandardInstructions {
	@Json(name = "errorBuffer")
	private String _errorBuffer;
	@Json(name = "value")
	private String _value;

	public StandardInstructions() {
	}

	public String getErrorBuffer() {
		return _errorBuffer;
	}

	public String getValue() {
		return _value;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(StandardInstructions standardInstructions) {
		try {
			return Serializer.serializeObject(standardInstructions);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static StandardInstructions fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(StandardInstructions.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

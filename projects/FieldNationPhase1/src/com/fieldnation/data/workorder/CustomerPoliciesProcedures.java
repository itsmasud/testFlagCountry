package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomerPoliciesProcedures {
	@Json(name = "errorBuffer")
	private String _errorBuffer;
	@Json(name = "value")
	private String _value;

	public CustomerPoliciesProcedures() {
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

	public static JsonObject toJson(
			CustomerPoliciesProcedures customerPoliciesProcedures) {
		try {
			return Serializer.serializeObject(customerPoliciesProcedures);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CustomerPoliciesProcedures fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(
					CustomerPoliciesProcedures.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

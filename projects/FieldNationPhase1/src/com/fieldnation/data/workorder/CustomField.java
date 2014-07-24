package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomField {
	@Json(name = "dateEntered")
	private String _dateEntered;
	@Json(name = "label")
	private String _label;
	@Json(name = "value")
	private String _value;
	@Json(name = "custom_label_id")
	private Integer _customLabelId;

	public CustomField() {
	}

	public String getDateEntered() {
		return _dateEntered;
	}

	public String getLabel() {
		return _label;
	}

	public String getValue() {
		return _value;
	}

	public Integer getCustomLabelId() {
		return _customLabelId;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(CustomField customFields) {
		try {
			return Serializer.serializeObject(customFields);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CustomField fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CustomField.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

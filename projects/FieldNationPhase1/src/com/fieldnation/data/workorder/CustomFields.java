package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomFields{
	@Json(name="dateEntered")
	private String _dateEntered;
	@Json(name="label")
	private String _label;
	@Json(name="value")
	private String _value;
	@Json(name="custom_label_id")
	private int _customLabelId;

	public CustomFields(){
	}
	public String getDateEntered(){
		return _dateEntered;
	}

	public String getLabel(){
		return _label;
	}

	public String getValue(){
		return _value;
	}

	public int getCustomLabelId(){
		return _customLabelId;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(CustomFields customFields) {
		try {
			return Serializer.serializeObject(customFields);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CustomFields fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CustomFields.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

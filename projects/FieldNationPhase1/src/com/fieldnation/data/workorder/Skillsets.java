package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Skillsets {
	@Json(name = "name")
	private String _name;
	@Json(name = "dynamic_term_id")
	private Integer _dynamicTermId;

	public Skillsets() {
	}

	public String getName() {
		return _name;
	}

	public Integer getDynamicTermId() {
		return _dynamicTermId;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Skillsets skillsets) {
		try {
			return Serializer.serializeObject(skillsets);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Skillsets fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Skillsets.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

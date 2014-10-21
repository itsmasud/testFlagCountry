package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Penalties{
	@Json(name="name")
	private String _name;
	@Json(name="amount")
	private Double _amount;

	public Penalties(){
	}
	public String getName(){
		return _name;
	}

	public Double getAmount(){
		return _amount;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Penalties penalties) {
		try {
			return Serializer.serializeObject(penalties);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Penalties fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Penalties.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

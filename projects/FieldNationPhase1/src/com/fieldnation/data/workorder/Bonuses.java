package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Bonuses{
	@Json(name="name")
	private String _name;
	@Json(name="amount")
	private Double _amount;

	public Bonuses(){
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

	public static JsonObject toJson(Bonuses bonuses) {
		try {
			return Serializer.serializeObject(bonuses);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Bonuses fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Bonuses.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

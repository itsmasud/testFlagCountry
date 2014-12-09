package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Penalty{
	@Json(name="amount")
	private Double _amount;
	@Json(name="name")
	private String _name;

	public Penalty(){
	}
	public Double getAmount(){
		return _amount;
	}

	public String getName(){
		return _name;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Penalty penalty) {
		try {
			return Serializer.serializeObject(penalty);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Penalty fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Penalty.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

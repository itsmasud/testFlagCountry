package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Discounts{
	@Json(name="amount")
	private double _amount;
	@Json(name="description")
	private String _description;

	public Discounts(){
	}
	public double getAmount(){
		return _amount;
	}

	public String getDescription(){
		return _description;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Discounts discounts) {
		try {
			return Serializer.serializeObject(discounts);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Discounts fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Discounts.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

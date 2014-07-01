package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class AdditionalExpenses{
	@Json(name="category_id")
	private int _categoryId;
	@Json(name="price")
	private double _price;
	@Json(name="description")
	private String _description;
	@Json(name="approved")
	private boolean _approved;

	public AdditionalExpenses(){
	}
	public int getCategoryId(){
		return _categoryId;
	}

	public double getPrice(){
		return _price;
	}

	public String getDescription(){
		return _description;
	}

	public boolean getApproved(){
		return _approved;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(AdditionalExpenses additionalExpenses) {
		try {
			return Serializer.serializeObject(additionalExpenses);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static AdditionalExpenses fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(AdditionalExpenses.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

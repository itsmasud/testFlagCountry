package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class AdditionalExpense {
	@Json(name = "description")
	private String _description;
	@Json(name = "price")
	private Double _price;
	@Json(name = "category_id")
	private Integer _categoryId;
	@Json(name = "approved")
	private Boolean _approved;

	public AdditionalExpense() {
	}

	public String getDescription() {
		return _description;
	}

	public Double getPrice() {
		return _price;
	}

	public Integer getCategoryId() {
		return _categoryId;
	}

	public Boolean getApproved() {
		return _approved;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(AdditionalExpense additionalExpenses) {
		try {
			return Serializer.serializeObject(additionalExpenses);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static AdditionalExpense fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(AdditionalExpense.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

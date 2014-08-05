package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class AdditionalExpense {
	@Json(name="expenseId")
	private Integer _expenseId;
	@Json(name = "description")
	private String _description;
	@Json(name = "price")
	private Double _price;
	@Json(name = "approved")
	private Boolean _approved;
	@Json(name="categoryId")
	private Integer _categoryId;

	public AdditionalExpense() {
	}
	public Integer getExpenseId(){
		return _expenseId;
	}

	public String getDescription() {
		return _description;
	}

	public Double getPrice() {
		return _price;
	}

	public Boolean getApproved() {
		return _approved;
	}

	public int getCategoryId(){
		return _categoryId;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(AdditionalExpense additionalExpense) {
		try {
			return Serializer.serializeObject(additionalExpense);
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

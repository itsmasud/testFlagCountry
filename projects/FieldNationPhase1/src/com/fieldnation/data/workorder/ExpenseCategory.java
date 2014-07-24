package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ExpenseCategory{
	@Json(name="name")
	private String _name;
	@Json(name="id")
	private Integer _id;

	public ExpenseCategory(){
	}
	public String getName(){
		return _name;
	}

	public Integer getId(){
		return _id;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(ExpenseCategory expenseCategory) {
		try {
			return Serializer.serializeObject(expenseCategory);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ExpenseCategory fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ExpenseCategory.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

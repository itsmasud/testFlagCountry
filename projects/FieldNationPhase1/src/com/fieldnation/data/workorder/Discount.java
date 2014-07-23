package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Discount{
	@Json(name="discount_id")
	private Integer _discountId;
	@Json(name = "amount")
	private Double _amount;
	@Json(name = "description")
	private String _description;

	public Discount(){
	}
	public Integer getDiscountId(){
		return _discountId;
	}

	public Double getAmount() {
		return _amount;
	}

	public String getDescription() {
		return _description;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Discount discounts) {
		try {
			return Serializer.serializeObject(discounts);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Discount fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Discount.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

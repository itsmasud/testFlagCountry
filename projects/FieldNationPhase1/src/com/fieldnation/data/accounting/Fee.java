package com.fieldnation.data.accounting;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Fee {
	@Json(name = "amount")
	private Double _amount;
	@Json(name = "description")
	private String _description;
	@Json(name = "workorder_id")
	private Integer _workorderId;

	public Fee() {
	}

	public Double getAmount() {
		return _amount;
	}

	public String getDescription() {
		return _description;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Fee fee) {
		try {
			return Serializer.serializeObject(fee);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Fee fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Fee.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

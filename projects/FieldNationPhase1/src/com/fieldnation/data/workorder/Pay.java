package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Pay {
	@Json(name = "bonuses")
	private double _bonuses;
	@Json(name = "maximumAmount")
	private double _maximumAmount;
	@Json(name = "expenses")
	private int _expenses;
	@Json(name = "fixedAmount")
	private int _fixedAmount;
	@Json(name = "maxHour")
	private int _maxHour;
	@Json(name = "perHour")
	private int _perHour;
	@Json(name = "basis")
	private String _basis;

	public Pay() {
	}

	public double getBonuses() {
		return _bonuses;
	}

	public double getMaximumAmount() {
		return _maximumAmount;
	}

	public int getExpenses() {
		return _expenses;
	}

	public int getFixedAmount() {
		return _fixedAmount;
	}

	public int getMaxHour() {
		return _maxHour;
	}

	public int getPerHour() {
		return _perHour;
	}

	public String getBasis() {
		return _basis;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Pay pay) {
		try {
			return Serializer.serializeObject(pay);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Pay fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Pay.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

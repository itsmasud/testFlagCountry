package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Pay {
	@Json(name = "maxDevice")
	private Integer _maxDevice;
	@Json(name = "fixedAmount")
	private Double _fixedAmount;
	@Json(name = "bonuses")
	private Double _bonuses;
	@Json(name = "maxHour")
	private Double _maxHour;
	@Json(name = "blendedAdditionalHours")
	private Double _blendedAdditionalHours;
	@Json(name = "perDevice")
	private Double _perDevice;
	@Json(name = "blendedFirstHours")
	private Double _blendedFirstHours;
	@Json(name = "perHour")
	private Double _perHour;
	@Json(name = "basis")
	private String _basis;
	@Json(name = "blendedStartRate")
	private Double _blendedStartRate;
	@Json(name = "maximumAmount")
	private Double _maximumAmount;
	@Json(name = "expenses")
	private Double _expenses;
	@Json(name = "payRateBasis")
	private String _payRateBasis;
	@Json(name = "blendedAdditionalRate")
	private Double _blendedAdditionalRate;

	public Pay() {
	}

	public Integer getMaxDevice() {
		return _maxDevice;
	}

	public Double getFixedAmount() {
		return _fixedAmount;
	}

	public Double getBonuses() {
		return _bonuses;
	}

	public Double getMaxHour() {
		return _maxHour;
	}

	public Double getBlendedAdditionalHours() {
		return _blendedAdditionalHours;
	}

	public Double getPerDevice() {
		return _perDevice;
	}

	public Double getBlendedFirstHours() {
		return _blendedFirstHours;
	}

	public Double getPerHour() {
		return _perHour;
	}

	public String getBasis() {
		return _basis;
	}

	public Double getBlendedStartRate() {
		return _blendedStartRate;
	}

	public Double getMaximumAmount() {
		return _maximumAmount;
	}

	public Double getExpenses() {
		return _expenses;
	}

	public String getPayRateBasis() {
		return _payRateBasis;
	}

	public Double getBlendedAdditionalRate() {
		return _blendedAdditionalRate;
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

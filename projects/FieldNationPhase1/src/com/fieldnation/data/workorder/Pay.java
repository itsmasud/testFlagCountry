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
	private Integer _bonuses;
	@Json(name = "maxHour")
	private Integer _maxHour;
	@Json(name = "blendedAdditionalHours")
	private Integer _blendedAdditionalHours;
	@Json(name = "perDevice")
	private Integer _perDevice;
	@Json(name = "blendedFirstHours")
	private Integer _blendedFirstHours;
	@Json(name = "perHour")
	private Integer _perHour;
	@Json(name = "basis")
	private String _basis;
	@Json(name = "blendedStartRate")
	private Integer _blendedStartRate;
	@Json(name = "maximumAmount")
	private Integer _maximumAmount;
	@Json(name = "expenses")
	private Integer _expenses;
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

	public Integer getBonuses() {
		return _bonuses;
	}

	public Integer getMaxHour() {
		return _maxHour;
	}

	public Integer getBlendedAdditionalHours() {
		return _blendedAdditionalHours;
	}

	public Integer getPerDevice() {
		return _perDevice;
	}

	public Integer getBlendedFirstHours() {
		return _blendedFirstHours;
	}

	public Integer getPerHour() {
		return _perHour;
	}

	public String getBasis() {
		return _basis;
	}

	public Integer getBlendedStartRate() {
		return _blendedStartRate;
	}

	public Integer getMaximumAmount() {
		return _maximumAmount;
	}

	public Integer getExpenses() {
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

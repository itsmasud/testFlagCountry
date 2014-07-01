package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Pay{
	@Json(name="maxDevice")
	private int _maxDevice;
	@Json(name="fixedAmount")
	private int _fixedAmount;
	@Json(name="bonuses")
	private int _bonuses;
	@Json(name="maxHour")
	private int _maxHour;
	@Json(name="blendedAdditionalHours")
	private int _blendedAdditionalHours;
	@Json(name="perDevice")
	private int _perDevice;
	@Json(name="blendedFirstHours")
	private int _blendedFirstHours;
	@Json(name="perHour")
	private int _perHour;
	@Json(name="basis")
	private String _basis;
	@Json(name="blendedStartRate")
	private int _blendedStartRate;
	@Json(name="maximumAmount")
	private int _maximumAmount;
	@Json(name="expenses")
	private int _expenses;
	@Json(name="payRateBasis")
	private String _payRateBasis;
	@Json(name="blendedAdditionalRate")
	private int _blendedAdditionalRate;

	public Pay(){
	}
	public int getMaxDevice(){
		return _maxDevice;
	}

	public int getFixedAmount(){
		return _fixedAmount;
	}

	public int getBonuses(){
		return _bonuses;
	}

	public int getMaxHour(){
		return _maxHour;
	}

	public int getBlendedAdditionalHours(){
		return _blendedAdditionalHours;
	}

	public int getPerDevice(){
		return _perDevice;
	}

	public int getBlendedFirstHours(){
		return _blendedFirstHours;
	}

	public int getPerHour(){
		return _perHour;
	}

	public String getBasis(){
		return _basis;
	}

	public int getBlendedStartRate(){
		return _blendedStartRate;
	}

	public int getMaximumAmount(){
		return _maximumAmount;
	}

	public int getExpenses(){
		return _expenses;
	}

	public String getPayRateBasis(){
		return _payRateBasis;
	}

	public int getBlendedAdditionalRate(){
		return _blendedAdditionalRate;
	}

	public JsonObject toJson(){
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

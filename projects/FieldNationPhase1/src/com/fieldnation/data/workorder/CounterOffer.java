package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOffer{
	@Json(name="payBasis")
	private String _payBasis;
	@Json(name="hourlyRate")
	private Double _hourlyRate;
	@Json(name="payPerDevice")
	private Double _payPerDevice;
	@Json(name="expires")
	private Boolean _expires;
	@Json(name="additionalMaxHours")
	private Double _additionalMaxHours;
	@Json(name="maxHours")
	private Double _maxHours;
	@Json(name="additionalHourRate")
	private Double _additionalHourRate;
	@Json(name="fixedHourRate")
	private Double _fixedHourRate;
	@Json(name="fixedHours")
	private Double _fixedHours;
	@Json(name="fixedTotalAmount")
	private Double _fixedTotalAmount;
	@Json(name="expiresAfter")
	private String _expiresAfter;
	@Json(name="maxDevices")
	private Double _maxDevices;

	public CounterOffer(){
	}
	public String getPayBasis(){
		return _payBasis;
	}

	public Double getHourlyRate(){
		return _hourlyRate;
	}

	public Double getPayPerDevice(){
		return _payPerDevice;
	}

	public Boolean getExpires(){
		return _expires;
	}

	public Double getAdditionalMaxHours(){
		return _additionalMaxHours;
	}

	public Double getMaxHours(){
		return _maxHours;
	}

	public Double getAdditionalHourRate(){
		return _additionalHourRate;
	}

	public Double getFixedHourRate(){
		return _fixedHourRate;
	}

	public Double getFixedHours(){
		return _fixedHours;
	}

	public Double getFixedTotalAmount(){
		return _fixedTotalAmount;
	}

	public String getExpiresAfter(){
		return _expiresAfter;
	}

	public Double getMaxDevices(){
		return _maxDevices;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(CounterOffer counterOffer) {
		try {
			return Serializer.serializeObject(counterOffer);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CounterOffer fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CounterOffer.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

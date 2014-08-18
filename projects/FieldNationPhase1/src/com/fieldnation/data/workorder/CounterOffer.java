package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOffer{
	@Json(name="expiresAfter")
	private String _expiresAfter;
	@Json(name="fixedTotalAmount")
	private Double _fixedTotalAmount;
	@Json(name="expires")
	private Boolean _expires;
	@Json(name="maxHours")
	private Double _maxHours;
	@Json(name="payBasis")
	private String _payBasis;
	@Json(name="hourlyRate")
	private Double _hourlyRate;

	public CounterOffer(){
	}
	public String getExpiresAfter(){
		return _expiresAfter;
	}

	public Double getFixedTotalAmount(){
		return _fixedTotalAmount;
	}

	public Boolean getExpires(){
		return _expires;
	}

	public Double getMaxHours(){
		return _maxHours;
	}

	public String getPayBasis(){
		return _payBasis;
	}

	public Double getHourlyRate(){
		return _hourlyRate;
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

package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOfferInfo {
	@Json(name = "explanation")
	private String _explanation;
	@Json(name = "expense")
	private AdditionalExpense[] _expense;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "schedule")
	private Schedule _schedule;
	@Json(name = "expiresAfter")
	private Integer _expiresAfter;
	@Json(name = "expires")
	private Boolean _expires;

	public CounterOfferInfo() {
	}

	public String getExplanation() {
		return _explanation;
	}

	public Integer getExpiresAfter() {
		return _expiresAfter;
	}

	public Boolean getExpires() {
		return _expires;
	}

	public AdditionalExpense[] getExpense() {
		return _expense;
	}

	public Pay getPay() {
		return _pay;
	}

	public Schedule getSchedule() {
		return _schedule;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(CounterOfferInfo counterOfferInfo) {
		try {
			return Serializer.serializeObject(counterOfferInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CounterOfferInfo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CounterOfferInfo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

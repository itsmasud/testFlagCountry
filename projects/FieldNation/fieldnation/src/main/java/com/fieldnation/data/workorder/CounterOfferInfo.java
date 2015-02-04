package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOfferInfo {
	@Json(name = "expense")
	private Expense[] _expense;
	@Json(name = "expires")
	private Boolean _expires;
	@Json(name = "expiresAfter")
	private String _expiresAfter;
	@Json(name="expiresAfterInSecond")
	private Integer _expiresAfterInSecond;
	@Json(name = "explanation")
	private String _explanation;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "schedule")
	private Schedule _schedule;

	public CounterOfferInfo() {
	}

	public Expense[] getExpense() {
		return _expense;
	}

	public Boolean getExpires() {
		return _expires;
	}

	public String getExpiresAfter() {
		return _expiresAfter;
	}

	public Integer getExpiresAfterInSecond(){
		return _expiresAfterInSecond;
	}

	public String getExplanation() {
		return _explanation;
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

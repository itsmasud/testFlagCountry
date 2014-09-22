package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOfferInfo{
	@Json(name="explanation")
	private String _explanation;
	@Json(name="expense")
	private Object[] _expense;
	@Json(name="pay")
	private Pay _pay;
	@Json(name="schedule")
	private Object[] _schedule;

	public CounterOfferInfo(){
	}
	public String getExplanation(){
		return _explanation;
	}

	public Object[] getExpense(){
		return _expense;
	}

	public Pay getPay(){
		return _pay;
	}

	public Object[] getSchedule(){
		return _schedule;
	}

	public JsonObject toJson(){
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

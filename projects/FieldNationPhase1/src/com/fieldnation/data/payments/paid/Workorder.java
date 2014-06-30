package com.fieldnation.data.payments.paid;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "wo_start_date")
	private String _woStartDate;
	@Json(name = "amount")
	private double _amount;
	@Json(name = "wo_end_date")
	private String _woEndDate;
	@Json(name = "description")
	private String _description;
	@Json(name = "wo_number")
	private int _woNumber;
	@Json(name = "wo_title")
	private String _woTitle;
	@Json(name = "client_name")
	private String _clientName;

	public Workorder() {
	}

	public String getWoStartDate() {
		return _woStartDate;
	}

	public double getAmount() {
		return _amount;
	}

	public String getWoEndDate() {
		return _woEndDate;
	}

	public String getDescription() {
		return _description;
	}

	public int getWoNumber() {
		return _woNumber;
	}

	public String getWoTitle() {
		return _woTitle;
	}

	public String getClientName() {
		return _clientName;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder workorder) {
		try {
			return Serializer.serializeObject(workorder);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

package com.fieldnation.data.accounting;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Payment {
	@Json(name = "status")
	private String _status;
	@Json(name = "date_paid")
	private String _datePaid;
	@Json(name = "amount")
	private Double _amount;
	@Json(name = "pay_method")
	private String _payMethod;
	@Json(name = "payment_id")
	private Integer _paymentId;
	@Json(name = "workorders")
	private Workorder[] _workorders;

	public Payment() {
	}

	public String getStatus() {
		return _status;
	}

	public String getDatePaid() {
		return _datePaid;
	}

	public Double getAmount(){
		return _amount;
	}

	public String getPayMethod() {
		return _payMethod;
	}

	public Integer getPaymentId(){
		return _paymentId;
	}

	public Workorder[] getWorkorders() {
		return _workorders;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Payment payment) {
		try {
			return Serializer.serializeObject(payment);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Payment fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Payment.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

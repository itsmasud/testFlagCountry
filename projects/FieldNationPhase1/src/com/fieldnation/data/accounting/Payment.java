package com.fieldnation.data.accounting;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Payment {
	@Json(name="paymentId")
	private Integer _paymentId;
	@Json(name="datePaid")
	private String _datePaid;
	@Json(name = "status")
	private String _status;
	@Json(name="fees")
	private Fee[] _fees;
	@Json(name = "amount")
	private Double _amount;
	@Json(name="payMethod")
	private String _payMethod;
	@Json(name = "workorders")
	private Workorder[] _workorders;

	public Payment() {
	}
	public Integer getPaymentId(){
		return _paymentId;
	}

	public String getDatePaid() {
		return _datePaid;
	}

	public String getStatus(){
		return _status;
	}

	public Fee[] getFees(){
		return _fees;
	}

	public Double getAmount(){
		return _amount;
	}

	public String getPayMethod() {
		return _payMethod;
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

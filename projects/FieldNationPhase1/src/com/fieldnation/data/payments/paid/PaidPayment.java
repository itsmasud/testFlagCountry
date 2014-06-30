package com.fieldnation.data.payments.paid;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class PaidPayment {
	@Json(name = "status")
	private String _status;
	@Json(name = "date_paid")
	private String _datePaid;
	@Json(name = "amount")
	private double _amount;
	@Json(name = "pay_method")
	private String _payMethod;
	@Json(name = "payment_id")
	private int _paymentId;
	@Json(name = "workorders")
	private Workorder[] _workorders;

	public PaidPayment() {
	}

	public String getStatus() {
		return _status;
	}

	public String getDatePaid() {
		return _datePaid;
	}

	public double getAmount() {
		return _amount;
	}

	public String getPayMethod() {
		return _payMethod;
	}

	public int getPaymentId() {
		return _paymentId;
	}

	public Workorder[] getWorkorders() {
		return _workorders;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(PaidPayment paidPayment) {
		try {
			return Serializer.serializeObject(paidPayment);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static PaidPayment fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(PaidPayment.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

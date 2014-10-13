package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ExpectedPayment {
	@Json(name = "bonuses")
	private Bonuses[] _bonuses;
	@Json(name="discounts")
	private Integer _discounts;
	@Json(name = "expectedAmount")
	private Double _expectedAmount;
	@Json(name = "expectedFee")
	private Double _expectedFee;
	@Json(name="expectedTotal")
	private Double _expectedTotal;
	@Json(name = "expensesApproved")
	private Double _expensesApproved;
	@Json(name="laborEarned")
	private Double _laborEarned;
	@Json(name="maxPayLimit")
	private Double _maxPayLimit;
	@Json(name = "paymentStatus")
	private String _paymentStatus;
	@Json(name="penalties")
	private Object _penalties;

	public ExpectedPayment() {
	}

	public Bonuses[] getBonuses() {
		return _bonuses;
	}

	public Integer getDiscounts(){
		return _discounts;
	}

	public Double getExpectedAmount() {
		return _expectedAmount;
	}

	public Double getExpectedFee() {
		return _expectedFee;
	}

	public Double getExpectedTotal(){
		return _expectedTotal;
	}

	public Double getExpensesApproved() {
		return _expensesApproved;
	}

	public Double getLaborEarned(){
		return _laborEarned;
	}

	public Double getMaxPayLimit(){
		return _maxPayLimit;
	}

	public String getPaymentStatus() {
		return _paymentStatus;
	}

	public Object getPenalties(){
		return _penalties;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(ExpectedPayment expectedPayment) {
		try {
			return Serializer.serializeObject(expectedPayment);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ExpectedPayment fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ExpectedPayment.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}

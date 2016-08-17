package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class ExpectedPayment {
    private static final String TAG = "ExpectedPayment";

    @Json(name = "bonuses")
    private Bonus[] _bonuses;
    @Json(name = "discounts")
    private Double _discounts;
    @Json(name = "expectedAmount")
    private Double _expectedAmount;
    @Json(name = "expectedFee")
    private Double _expectedFee;
    @Json(name = "expectedInsuranceFee")
    private Double _expectedInsuranceFee;
    @Json(name = "expectedServiceFee")
    private Double _expectedServiceFee;
    @Json(name = "expectedTotal")
    private Double _expectedTotal;
    @Json(name = "expensesApproved")
    private Double _expensesApproved;
    @Json(name = "fees")
    private Fee[] _fees;
    @Json(name = "fnFeePercentage")
    private Float _fnFeePercentage;
    @Json(name = "laborEarned")
    private Double _laborEarned;
    @Json(name = "maxPayLimit")
    private Double _maxPayLimit;
    @Json(name = "paymentStatus")
    private String _paymentStatus;
    @Json(name = "penalties")
    private Penalty[] _penalties;

    public ExpectedPayment() {
    }

    public Bonus[] getBonuses() {
        return _bonuses;
    }

    public Double getDiscounts() {
        return _discounts == null ? 0.0 : _discounts;
    }

    public Double getExpectedAmount() {
        return _expectedAmount == null ? 0.0 : _expectedAmount;
    }

    public Double getExpectedFee() {
        return _expectedFee == null ? 0.0 : _expectedFee;

    }

    public Double getExpectedInsuranceFee() {
        return _expectedInsuranceFee == null ? 0.0 : _expectedInsuranceFee;

    }

    public Double getExpectedServiceFee() {
        return _expectedServiceFee == null ? 0.0 : _expectedServiceFee;

    }

    public Double getExpectedTotal() {
        return _expectedTotal == null ? 0.0 : _expectedTotal;
    }

    public Double getExpensesApproved() {
        return _expensesApproved == null ? 0.0 : _expensesApproved;

    }

    public Fee[] getFees() {
        return _fees;
    }

    public Float getFnFeePercentage() {
        return _fnFeePercentage == null ? 0.0f : _fnFeePercentage;

    }

    public Double getLaborEarned() {
        return _laborEarned == null ? 0.0 : _laborEarned;
    }

    public Double getMaxPayLimit() {
        return _maxPayLimit == null ? 0.0 : _maxPayLimit;
    }

    public String getPaymentStatus() {
        return _paymentStatus;
    }

    public Penalty[] getPenalties() {
        return _penalties;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ExpectedPayment expectedPayment) {
        try {
            return Serializer.serializeObject(expectedPayment);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ExpectedPayment fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(ExpectedPayment.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

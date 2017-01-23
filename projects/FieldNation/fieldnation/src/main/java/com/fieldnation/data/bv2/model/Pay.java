package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Pay {
    private static final String TAG = "Pay";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "type")
    private String type = null;

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "base")
    private PayBase base = null;

    @Json(name = "additional")
    private PayBase additional = null;

    @Json(name = "finance")
    private PayFinance finance = null;

    @Json(name = "range")
    private PayRange range = null;

    @Json(name = "status_id")
    private Integer statusId = null;

    @Json(name = "pricing_insights")
    private PricingInsights pricingInsights = null;

    @Json(name = "reported_hours")
    private Double reportedHours = null;

    @Json(name = "number_of_devices")
    private Double numberOfDevices = null;

    @Json(name = "labor_sum")
    private Double laborSum = null;

    @Json(name = "total")
    private Double total = null;

    @Json(name = "estimated_payment_date")
    private String estimatedPaymentDate = null;

    @Json(name = "hold")
    private Fee hold = null;

    @Json(name = "payment")
    private Fee payment = null;

    @Json(name = "fees")
    private PayFees fees = null;

    public Pay() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String getType() {
        return type;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getRole() {
        return role;
    }

    public String[] getActions() {
        return actions;
    }

    public PayBase getBase() {
        return base;
    }

    public PayBase getAdditional() {
        return additional;
    }

    public PayFinance getFinance() {
        return finance;
    }

    public PayRange getRange() {
        return range;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public PricingInsights getPricingInsights() {
        return pricingInsights;
    }

    public Double getReportedHours() {
        return reportedHours;
    }

    public Double getNumberOfDevices() {
        return numberOfDevices;
    }

    public Double getLaborSum() {
        return laborSum;
    }

    public Double getTotal() {
        return total;
    }

    public String getEstimatedPaymentDate() {
        return estimatedPaymentDate;
    }

    public Fee getHold() {
        return hold;
    }

    public Fee getPayment() {
        return payment;
    }

    public PayFees getFees() {
        return fees;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Pay fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Pay.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Pay pay) {
        try {
            return Serializer.serializeObject(pay);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
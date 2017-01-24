package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Pay {
    private static final String TAG = "Pay";

    @Json(name = "number_of_devices")
    private Double numberOfDevices;

    @Json(name = "fees")
    private PayFees fees;

    @Json(name = "role")
    private String role;

    @Json(name = "estimated_payment_date")
    private Date estimatedPaymentDate;

    @Json(name = "additional")
    private PayBase additional;

    @Json(name = "range")
    private PayRange range;

    @Json(name = "type")
    private String type;

    @Json(name = "labor_sum")
    private Double laborSum;

    @Json(name = "pricing_insights")
    private PricingInsights pricingInsights;

    @Json(name = "hold")
    private Fee hold;

    @Json(name = "total")
    private Double total;

    @Json(name = "status_id")
    private Integer statusId;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    @Json(name = "correlation_id")
    private String correlationId;

    @Json(name = "payment")
    private Fee payment;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "finance")
    private PayFinance finance;

    @Json(name = "reported_hours")
    private Double reportedHours;

    @Json(name = "base")
    private PayBase base;

    public Pay() {
    }

    public Double getNumberOfDevices() {
        return numberOfDevices;
    }

    public PayFees getFees() {
        return fees;
    }

    public String getRole() {
        return role;
    }

    public Date getEstimatedPaymentDate() {
        return estimatedPaymentDate;
    }

    public PayBase getAdditional() {
        return additional;
    }

    public PayRange getRange() {
        return range;
    }

    public String getType() {
        return type;
    }

    public Double getLaborSum() {
        return laborSum;
    }

    public PricingInsights getPricingInsights() {
        return pricingInsights;
    }

    public Fee getHold() {
        return hold;
    }

    public Double getTotal() {
        return total;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Fee getPayment() {
        return payment;
    }

    public String[] getActions() {
        return actions;
    }

    public PayFinance getFinance() {
        return finance;
    }

    public Double getReportedHours() {
        return reportedHours;
    }

    public PayBase getBase() {
        return base;
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

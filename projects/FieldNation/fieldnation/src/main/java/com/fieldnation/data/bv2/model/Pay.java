package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Pay {
    private static final String TAG = "Pay";

    @Json(name = "number_of_devices")
    private Double _numberOfDevices;

    @Json(name = "fees")
    private PayFees _fees;

    @Json(name = "role")
    private String _role;

    @Json(name = "estimated_payment_date")
    private Date _estimatedPaymentDate;

    @Json(name = "additional")
    private PayAdditional _additional;

    @Json(name = "range")
    private PayRange _range;

    @Json(name = "type")
    private String _type;

    @Json(name = "labor_sum")
    private Double _laborSum;

    @Json(name = "pricing_insights")
    private PricingInsights _pricingInsights;

    @Json(name = "hold")
    private Fee _hold;

    @Json(name = "total")
    private Double _total;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "payment")
    private Fee _payment;

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "finance")
    private PayFinance _finance;

    @Json(name = "reported_hours")
    private Double _reportedHours;

    @Json(name = "base")
    private PayBase _base;

    public Pay() {
    }

    public Double getNumberOfDevices() {
        return _numberOfDevices;
    }

    public PayFees getFees() {
        return _fees;
    }

    public String getRole() {
        return _role;
    }

    public Date getEstimatedPaymentDate() {
        return _estimatedPaymentDate;
    }

    public PayAdditional getAdditional() {
        return _additional;
    }

    public PayRange getRange() {
        return _range;
    }

    public String getType() {
        return _type;
    }

    public Double getLaborSum() {
        return _laborSum;
    }

    public PricingInsights getPricingInsights() {
        return _pricingInsights;
    }

    public Fee getHold() {
        return _hold;
    }

    public Double getTotal() {
        return _total;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Fee getPayment() {
        return _payment;
    }

    public String[] getActions() {
        return _actions;
    }

    public PayFinance getFinance() {
        return _finance;
    }

    public Double getReportedHours() {
        return _reportedHours;
    }

    public PayBase getBase() {
        return _base;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Pay fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Pay.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

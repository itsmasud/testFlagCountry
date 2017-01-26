package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class Pay implements Parcelable {
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

    public void setNumberOfDevices(Double numberOfDevices) {
        _numberOfDevices = numberOfDevices;
    }

    public Double getNumberOfDevices() {
        return _numberOfDevices;
    }

    public Pay numberOfDevices(Double numberOfDevices) {
        _numberOfDevices = numberOfDevices;
        return this;
    }

    public void setFees(PayFees fees) {
        _fees = fees;
    }

    public PayFees getFees() {
        return _fees;
    }

    public Pay fees(PayFees fees) {
        _fees = fees;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Pay role(String role) {
        _role = role;
        return this;
    }

    public void setEstimatedPaymentDate(Date estimatedPaymentDate) {
        _estimatedPaymentDate = estimatedPaymentDate;
    }

    public Date getEstimatedPaymentDate() {
        return _estimatedPaymentDate;
    }

    public Pay estimatedPaymentDate(Date estimatedPaymentDate) {
        _estimatedPaymentDate = estimatedPaymentDate;
        return this;
    }

    public void setAdditional(PayAdditional additional) {
        _additional = additional;
    }

    public PayAdditional getAdditional() {
        return _additional;
    }

    public Pay additional(PayAdditional additional) {
        _additional = additional;
        return this;
    }

    public void setRange(PayRange range) {
        _range = range;
    }

    public PayRange getRange() {
        return _range;
    }

    public Pay range(PayRange range) {
        _range = range;
        return this;
    }

    public void setType(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

    public Pay type(String type) {
        _type = type;
        return this;
    }

    public void setLaborSum(Double laborSum) {
        _laborSum = laborSum;
    }

    public Double getLaborSum() {
        return _laborSum;
    }

    public Pay laborSum(Double laborSum) {
        _laborSum = laborSum;
        return this;
    }

    public void setPricingInsights(PricingInsights pricingInsights) {
        _pricingInsights = pricingInsights;
    }

    public PricingInsights getPricingInsights() {
        return _pricingInsights;
    }

    public Pay pricingInsights(PricingInsights pricingInsights) {
        _pricingInsights = pricingInsights;
        return this;
    }

    public void setHold(Fee hold) {
        _hold = hold;
    }

    public Fee getHold() {
        return _hold;
    }

    public Pay hold(Fee hold) {
        _hold = hold;
        return this;
    }

    public void setTotal(Double total) {
        _total = total;
    }

    public Double getTotal() {
        return _total;
    }

    public Pay total(Double total) {
        _total = total;
        return this;
    }

    public void setStatusId(Integer statusId) {
        _statusId = statusId;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Pay statusId(Integer statusId) {
        _statusId = statusId;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public Pay workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Pay correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setPayment(Fee payment) {
        _payment = payment;
    }

    public Fee getPayment() {
        return _payment;
    }

    public Pay payment(Fee payment) {
        _payment = payment;
        return this;
    }

    public void setActions(String[] actions) {
        _actions = actions;
    }

    public String[] getActions() {
        return _actions;
    }

    public Pay actions(String[] actions) {
        _actions = actions;
        return this;
    }

    public void setFinance(PayFinance finance) {
        _finance = finance;
    }

    public PayFinance getFinance() {
        return _finance;
    }

    public Pay finance(PayFinance finance) {
        _finance = finance;
        return this;
    }

    public void setReportedHours(Double reportedHours) {
        _reportedHours = reportedHours;
    }

    public Double getReportedHours() {
        return _reportedHours;
    }

    public Pay reportedHours(Double reportedHours) {
        _reportedHours = reportedHours;
        return this;
    }

    public void setBase(PayBase base) {
        _base = base;
    }

    public PayBase getBase() {
        return _base;
    }

    public Pay base(PayBase base) {
        _base = base;
        return this;
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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Pay> CREATOR = new Parcelable.Creator<Pay>() {

        @Override
        public Pay createFromParcel(Parcel source) {
            try {
                return Pay.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Pay[] newArray(int size) {
            return new Pay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}

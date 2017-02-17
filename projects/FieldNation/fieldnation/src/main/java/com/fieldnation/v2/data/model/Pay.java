package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Pay implements Parcelable {
    private static final String TAG = "Pay";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "additional")
    private PayAdditional _additional;

    @Json(name = "base")
    private PayBase _base;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "estimated_payment_date")
    private Date _estimatedPaymentDate;

    @Json(name = "expenses")
    private Expenses _expenses;

    @Json(name = "fees")
    private PayFees _fees;

    @Json(name = "finance")
    private PayFinance _finance;

    @Json(name = "hold")
    private PayModifier _hold;

    @Json(name = "increases")
    private PayIncreases _increases;

    @Json(name = "labor_sum")
    private Double _laborSum;

    @Json(name = "number_of_devices")
    private Double _numberOfDevices;

    @Json(name = "payment")
    private PayModifier _payment;

    @Json(name = "pricing_insights")
    private PricingInsights _pricingInsights;

    @Json(name = "range")
    private PayRange _range;

    @Json(name = "reported_hours")
    private Double _reportedHours;

    @Json(name = "role")
    private String _role;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "total")
    private Double _total;

    @Json(name = "type")
    private String _type;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Pay() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Pay actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAdditional(PayAdditional additional) throws ParseException {
        _additional = additional;
        SOURCE.put("additional", additional.getJson());
    }

    public PayAdditional getAdditional() {
        return _additional;
    }

    public Pay additional(PayAdditional additional) throws ParseException {
        _additional = additional;
        SOURCE.put("additional", additional.getJson());
        return this;
    }

    public void setBase(PayBase base) throws ParseException {
        _base = base;
        SOURCE.put("base", base.getJson());
    }

    public PayBase getBase() {
        return _base;
    }

    public Pay base(PayBase base) throws ParseException {
        _base = base;
        SOURCE.put("base", base.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Pay correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setEstimatedPaymentDate(Date estimatedPaymentDate) throws ParseException {
        _estimatedPaymentDate = estimatedPaymentDate;
        SOURCE.put("estimated_payment_date", estimatedPaymentDate.getJson());
    }

    public Date getEstimatedPaymentDate() {
        return _estimatedPaymentDate;
    }

    public Pay estimatedPaymentDate(Date estimatedPaymentDate) throws ParseException {
        _estimatedPaymentDate = estimatedPaymentDate;
        SOURCE.put("estimated_payment_date", estimatedPaymentDate.getJson());
        return this;
    }

    public void setExpenses(Expenses expenses) throws ParseException {
        _expenses = expenses;
        SOURCE.put("expenses", expenses.getJson());
    }

    public Expenses getExpenses() {
        return _expenses;
    }

    public Pay expenses(Expenses expenses) throws ParseException {
        _expenses = expenses;
        SOURCE.put("expenses", expenses.getJson());
        return this;
    }

    public void setFees(PayFees fees) throws ParseException {
        _fees = fees;
        SOURCE.put("fees", fees.getJson());
    }

    public PayFees getFees() {
        return _fees;
    }

    public Pay fees(PayFees fees) throws ParseException {
        _fees = fees;
        SOURCE.put("fees", fees.getJson());
        return this;
    }

    public void setFinance(PayFinance finance) throws ParseException {
        _finance = finance;
        SOURCE.put("finance", finance.getJson());
    }

    public PayFinance getFinance() {
        return _finance;
    }

    public Pay finance(PayFinance finance) throws ParseException {
        _finance = finance;
        SOURCE.put("finance", finance.getJson());
        return this;
    }

    public void setHold(PayModifier hold) throws ParseException {
        _hold = hold;
        SOURCE.put("hold", hold.getJson());
    }

    public PayModifier getHold() {
        return _hold;
    }

    public Pay hold(PayModifier hold) throws ParseException {
        _hold = hold;
        SOURCE.put("hold", hold.getJson());
        return this;
    }

    public void setIncreases(PayIncreases increases) throws ParseException {
        _increases = increases;
        SOURCE.put("increases", increases.getJson());
    }

    public PayIncreases getIncreases() {
        return _increases;
    }

    public Pay increases(PayIncreases increases) throws ParseException {
        _increases = increases;
        SOURCE.put("increases", increases.getJson());
        return this;
    }

    public void setLaborSum(Double laborSum) throws ParseException {
        _laborSum = laborSum;
        SOURCE.put("labor_sum", laborSum);
    }

    public Double getLaborSum() {
        return _laborSum;
    }

    public Pay laborSum(Double laborSum) throws ParseException {
        _laborSum = laborSum;
        SOURCE.put("labor_sum", laborSum);
        return this;
    }

    public void setNumberOfDevices(Double numberOfDevices) throws ParseException {
        _numberOfDevices = numberOfDevices;
        SOURCE.put("number_of_devices", numberOfDevices);
    }

    public Double getNumberOfDevices() {
        return _numberOfDevices;
    }

    public Pay numberOfDevices(Double numberOfDevices) throws ParseException {
        _numberOfDevices = numberOfDevices;
        SOURCE.put("number_of_devices", numberOfDevices);
        return this;
    }

    public void setPayment(PayModifier payment) throws ParseException {
        _payment = payment;
        SOURCE.put("payment", payment.getJson());
    }

    public PayModifier getPayment() {
        return _payment;
    }

    public Pay payment(PayModifier payment) throws ParseException {
        _payment = payment;
        SOURCE.put("payment", payment.getJson());
        return this;
    }

    public void setPricingInsights(PricingInsights pricingInsights) throws ParseException {
        _pricingInsights = pricingInsights;
        SOURCE.put("pricing_insights", pricingInsights.getJson());
    }

    public PricingInsights getPricingInsights() {
        return _pricingInsights;
    }

    public Pay pricingInsights(PricingInsights pricingInsights) throws ParseException {
        _pricingInsights = pricingInsights;
        SOURCE.put("pricing_insights", pricingInsights.getJson());
        return this;
    }

    public void setRange(PayRange range) throws ParseException {
        _range = range;
        SOURCE.put("range", range.getJson());
    }

    public PayRange getRange() {
        return _range;
    }

    public Pay range(PayRange range) throws ParseException {
        _range = range;
        SOURCE.put("range", range.getJson());
        return this;
    }

    public void setReportedHours(Double reportedHours) throws ParseException {
        _reportedHours = reportedHours;
        SOURCE.put("reported_hours", reportedHours);
    }

    public Double getReportedHours() {
        return _reportedHours;
    }

    public Pay reportedHours(Double reportedHours) throws ParseException {
        _reportedHours = reportedHours;
        SOURCE.put("reported_hours", reportedHours);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        return _role;
    }

    public Pay role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setStatusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Pay statusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
        return this;
    }

    public void setTotal(Double total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
    }

    public Double getTotal() {
        return _total;
    }

    public Pay total(Double total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        return _type;
    }

    public Pay type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public Pay workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Pay[] array) {
        JsonArray list = new JsonArray();
        for (Pay item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Pay[] fromJsonArray(JsonArray array) {
        Pay[] list = new Pay[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Pay fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Pay.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

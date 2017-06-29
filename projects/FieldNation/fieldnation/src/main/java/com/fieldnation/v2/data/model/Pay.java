package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

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

    @Json(name = "bonuses")
    private PayModifiers _bonuses;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "discounts")
    private PayModifiers _discounts;

    @Json(name = "estimated_payment_date")
    private Date _estimatedPaymentDate;

    @Json(name = "expenses")
    private Expenses _expenses;

    @Json(name = "fees")
    private PayModifier[] _fees;

    @Json(name = "finance")
    private PayFinance _finance;

    @Json(name = "hold")
    private PayModifier _hold;

    @Json(name = "increases")
    private PayIncreases _increases;

    @Json(name = "labor_sum")
    private Double _laborSum;

    @Json(name = "max_pay")
    private Double _maxPay;

    @Json(name = "number_of_devices")
    private Double _numberOfDevices;

    @Json(name = "payment")
    private PayModifier _payment;

    @Json(name = "penalties")
    private PayModifiers _penalties;

    @Json(name = "pricing_insights")
    private PricingInsights _pricingInsights;

    @Json(name = "range")
    private PayRange _range;

    @Json(name = "reported_hours")
    private Double _reportedHours;

    @Json(name = "role")
    private String _role;

    @Json(name = "total")
    private Double _total;

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE;

    public Pay() {
        SOURCE = new JsonObject();
    }

    public Pay(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new ActionsEnum[0];

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
        try {
            if (_additional == null && SOURCE.has("additional") && SOURCE.get("additional") != null)
                _additional = PayAdditional.fromJson(SOURCE.getJsonObject("additional"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_additional == null)
            _additional = new PayAdditional();

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
        try {
            if (_base == null && SOURCE.has("base") && SOURCE.get("base") != null)
                _base = PayBase.fromJson(SOURCE.getJsonObject("base"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_base == null)
            _base = new PayBase();

        return _base;
    }

    public Pay base(PayBase base) throws ParseException {
        _base = base;
        SOURCE.put("base", base.getJson());
        return this;
    }

    public void setBonuses(PayModifiers bonuses) throws ParseException {
        _bonuses = bonuses;
        SOURCE.put("bonuses", bonuses.getJson());
    }

    public PayModifiers getBonuses() {
        try {
            if (_bonuses == null && SOURCE.has("bonuses") && SOURCE.get("bonuses") != null)
                _bonuses = PayModifiers.fromJson(SOURCE.getJsonObject("bonuses"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_bonuses == null)
            _bonuses = new PayModifiers();

        return _bonuses;
    }

    public Pay bonuses(PayModifiers bonuses) throws ParseException {
        _bonuses = bonuses;
        SOURCE.put("bonuses", bonuses.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public Pay correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setDiscounts(PayModifiers discounts) throws ParseException {
        _discounts = discounts;
        SOURCE.put("discounts", discounts.getJson());
    }

    public PayModifiers getDiscounts() {
        try {
            if (_discounts == null && SOURCE.has("discounts") && SOURCE.get("discounts") != null)
                _discounts = PayModifiers.fromJson(SOURCE.getJsonObject("discounts"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_discounts == null)
            _discounts = new PayModifiers();

        return _discounts;
    }

    public Pay discounts(PayModifiers discounts) throws ParseException {
        _discounts = discounts;
        SOURCE.put("discounts", discounts.getJson());
        return this;
    }

    public void setEstimatedPaymentDate(Date estimatedPaymentDate) throws ParseException {
        _estimatedPaymentDate = estimatedPaymentDate;
        SOURCE.put("estimated_payment_date", estimatedPaymentDate.getJson());
    }

    public Date getEstimatedPaymentDate() {
        try {
            if (_estimatedPaymentDate == null && SOURCE.has("estimated_payment_date") && SOURCE.get("estimated_payment_date") != null)
                _estimatedPaymentDate = Date.fromJson(SOURCE.getJsonObject("estimated_payment_date"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_estimatedPaymentDate == null)
            _estimatedPaymentDate = new Date();

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
        try {
            if (_expenses == null && SOURCE.has("expenses") && SOURCE.get("expenses") != null)
                _expenses = Expenses.fromJson(SOURCE.getJsonObject("expenses"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_expenses == null)
            _expenses = new Expenses();

        return _expenses;
    }

    public Pay expenses(Expenses expenses) throws ParseException {
        _expenses = expenses;
        SOURCE.put("expenses", expenses.getJson());
        return this;
    }

    public void setFees(PayModifier[] fees) throws ParseException {
        _fees = fees;
        SOURCE.put("fees", PayModifier.toJsonArray(fees));
    }

    public PayModifier[] getFees() {
        try {
            if (_fees != null)
                return _fees;

            if (SOURCE.has("fees") && SOURCE.get("fees") != null) {
                _fees = PayModifier.fromJsonArray(SOURCE.getJsonArray("fees"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_fees == null)
            _fees = new PayModifier[0];

        return _fees;
    }

    public Pay fees(PayModifier[] fees) throws ParseException {
        _fees = fees;
        SOURCE.put("fees", PayModifier.toJsonArray(fees), true);
        return this;
    }

    public void setFinance(PayFinance finance) throws ParseException {
        _finance = finance;
        SOURCE.put("finance", finance.getJson());
    }

    public PayFinance getFinance() {
        try {
            if (_finance == null && SOURCE.has("finance") && SOURCE.get("finance") != null)
                _finance = PayFinance.fromJson(SOURCE.getJsonObject("finance"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_finance == null)
            _finance = new PayFinance();

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
        try {
            if (_hold == null && SOURCE.has("hold") && SOURCE.get("hold") != null)
                _hold = PayModifier.fromJson(SOURCE.getJsonObject("hold"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_hold == null)
            _hold = new PayModifier();

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
        try {
            if (_increases == null && SOURCE.has("increases") && SOURCE.get("increases") != null)
                _increases = PayIncreases.fromJson(SOURCE.getJsonObject("increases"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_increases == null)
            _increases = new PayIncreases();

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
        try {
            if (_laborSum == null && SOURCE.has("labor_sum") && SOURCE.get("labor_sum") != null)
                _laborSum = SOURCE.getDouble("labor_sum");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _laborSum;
    }

    public Pay laborSum(Double laborSum) throws ParseException {
        _laborSum = laborSum;
        SOURCE.put("labor_sum", laborSum);
        return this;
    }

    public void setMaxPay(Double maxPay) throws ParseException {
        _maxPay = maxPay;
        SOURCE.put("max_pay", maxPay);
    }

    public Double getMaxPay() {
        try {
            if (_maxPay == null && SOURCE.has("max_pay") && SOURCE.get("max_pay") != null)
                _maxPay = SOURCE.getDouble("max_pay");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _maxPay;
    }

    public Pay maxPay(Double maxPay) throws ParseException {
        _maxPay = maxPay;
        SOURCE.put("max_pay", maxPay);
        return this;
    }

    public void setNumberOfDevices(Double numberOfDevices) throws ParseException {
        _numberOfDevices = numberOfDevices;
        SOURCE.put("number_of_devices", numberOfDevices);
    }

    public Double getNumberOfDevices() {
        try {
            if (_numberOfDevices == null && SOURCE.has("number_of_devices") && SOURCE.get("number_of_devices") != null)
                _numberOfDevices = SOURCE.getDouble("number_of_devices");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_payment == null && SOURCE.has("payment") && SOURCE.get("payment") != null)
                _payment = PayModifier.fromJson(SOURCE.getJsonObject("payment"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_payment == null)
            _payment = new PayModifier();

        return _payment;
    }

    public Pay payment(PayModifier payment) throws ParseException {
        _payment = payment;
        SOURCE.put("payment", payment.getJson());
        return this;
    }

    public void setPenalties(PayModifiers penalties) throws ParseException {
        _penalties = penalties;
        SOURCE.put("penalties", penalties.getJson());
    }

    public PayModifiers getPenalties() {
        try {
            if (_penalties == null && SOURCE.has("penalties") && SOURCE.get("penalties") != null)
                _penalties = PayModifiers.fromJson(SOURCE.getJsonObject("penalties"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_penalties == null)
            _penalties = new PayModifiers();

        return _penalties;
    }

    public Pay penalties(PayModifiers penalties) throws ParseException {
        _penalties = penalties;
        SOURCE.put("penalties", penalties.getJson());
        return this;
    }

    public void setPricingInsights(PricingInsights pricingInsights) throws ParseException {
        _pricingInsights = pricingInsights;
        SOURCE.put("pricing_insights", pricingInsights.getJson());
    }

    public PricingInsights getPricingInsights() {
        try {
            if (_pricingInsights == null && SOURCE.has("pricing_insights") && SOURCE.get("pricing_insights") != null)
                _pricingInsights = PricingInsights.fromJson(SOURCE.getJsonObject("pricing_insights"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_pricingInsights == null)
            _pricingInsights = new PricingInsights();

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
        try {
            if (_range == null && SOURCE.has("range") && SOURCE.get("range") != null)
                _range = PayRange.fromJson(SOURCE.getJsonObject("range"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_range == null)
            _range = new PayRange();

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
        try {
            if (_reportedHours == null && SOURCE.has("reported_hours") && SOURCE.get("reported_hours") != null)
                _reportedHours = SOURCE.getDouble("reported_hours");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public Pay role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setTotal(Double total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
    }

    public Double getTotal() {
        try {
            if (_total == null && SOURCE.has("total") && SOURCE.get("total") != null)
                _total = SOURCE.getDouble("total");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _total;
    }

    public Pay total(Double total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public Pay type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId == null && SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
    public enum TypeEnum {
        @Json(name = "blended")
        BLENDED("blended"),
        @Json(name = "device")
        DEVICE("device"),
        @Json(name = "fixed")
        FIXED("fixed"),
        @Json(name = "hourly")
        HOURLY("hourly");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new Pay(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public String[] toDisplayStringLong() {
        String line1 = null;
        String line2 = null;

        TypeEnum type = getType();

        // Todo, need to localize this
        try {
            switch (type) {
                case FIXED:
                    line1 = misc.toCurrency(getBase().getAmount()) + " Fixed";
                    break;
                case HOURLY:
                    line1 = misc.toCurrency(getBase().getAmount()) + " per hr up to " + getBase().getUnits() + " hours.";
                    break;
                case BLENDED:
                    line1 = misc.toCurrency(getBase().getAmount()) + " for the first " + getBase().getUnits() + " hours.";
                    line2 = "Then " + misc.toCurrency(getAdditional().getAmount()) + " per hr up to " + getAdditional().getUnits() + " hours.";
                    break;
                case DEVICE:
                    line1 = misc.toCurrency(getBase().getAmount()) + " per device up to " + getBase().getUnits() + " devices.";
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            line1 = null;
            line2 = null;
        }

        return new String[]{line1, line2};
    }

    public String toDisplayStringShort() {
        TypeEnum type = getType();

        try {
            // Todo, localize this
            switch (type) {
                case BLENDED:
                    return misc.toCurrency(getBase().getAmount());
                case DEVICE:
                    return misc.toCurrency(getBase().getAmount());
                case FIXED:
                    return misc.toCurrency(getBase().getAmount());
                case HOURLY:
                    return misc.toCurrency(getBase().getAmount());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }
}

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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class FundTransaction implements Parcelable {
    private static final String TAG = "FundTransaction";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "bank_details")
    private BankDetails _bankDetails;

    @Json(name = "billing_address")
    private BillingAddress _billingAddress;

    @Json(name = "created")
    private Date _created;

    @Json(name = "credit_card")
    private CreditCard _creditCard;

    @Json(name = "credit_card_id")
    private String _creditCardId;

    @Json(name = "description")
    private String _description;

    @Json(name = "external_id")
    private String _externalId;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "note")
    private String _note;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "status_reason")
    private String _statusReason;

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "updated")
    private Date _updated;

    @Json(name = "work_order")
    private WorkOrder _workOrder;

    @Source
    private JsonObject SOURCE;

    public FundTransaction() {
        SOURCE = new JsonObject();
    }

    public FundTransaction(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        try {
            if (_amount == null && SOURCE.has("amount") && SOURCE.get("amount") != null)
                _amount = SOURCE.getDouble("amount");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _amount;
    }

    public FundTransaction amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setBankDetails(BankDetails bankDetails) throws ParseException {
        _bankDetails = bankDetails;
        SOURCE.put("bank_details", bankDetails.getJson());
    }

    public BankDetails getBankDetails() {
        try {
            if (_bankDetails == null && SOURCE.has("bank_details") && SOURCE.get("bank_details") != null)
                _bankDetails = BankDetails.fromJson(SOURCE.getJsonObject("bank_details"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_bankDetails != null && _bankDetails.isSet())
        return _bankDetails;

        return null;
    }

    public FundTransaction bankDetails(BankDetails bankDetails) throws ParseException {
        _bankDetails = bankDetails;
        SOURCE.put("bank_details", bankDetails.getJson());
        return this;
    }

    public void setBillingAddress(BillingAddress billingAddress) throws ParseException {
        _billingAddress = billingAddress;
        SOURCE.put("billing_address", billingAddress.getJson());
    }

    public BillingAddress getBillingAddress() {
        try {
            if (_billingAddress == null && SOURCE.has("billing_address") && SOURCE.get("billing_address") != null)
                _billingAddress = BillingAddress.fromJson(SOURCE.getJsonObject("billing_address"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_billingAddress != null && _billingAddress.isSet())
        return _billingAddress;

        return null;
    }

    public FundTransaction billingAddress(BillingAddress billingAddress) throws ParseException {
        _billingAddress = billingAddress;
        SOURCE.put("billing_address", billingAddress.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
        return _created;

        return null;
    }

    public FundTransaction created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setCreditCard(CreditCard creditCard) throws ParseException {
        _creditCard = creditCard;
        SOURCE.put("credit_card", creditCard.getJson());
    }

    public CreditCard getCreditCard() {
        try {
            if (_creditCard == null && SOURCE.has("credit_card") && SOURCE.get("credit_card") != null)
                _creditCard = CreditCard.fromJson(SOURCE.getJsonObject("credit_card"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_creditCard != null && _creditCard.isSet())
        return _creditCard;

        return null;
    }

    public FundTransaction creditCard(CreditCard creditCard) throws ParseException {
        _creditCard = creditCard;
        SOURCE.put("credit_card", creditCard.getJson());
        return this;
    }

    public void setCreditCardId(String creditCardId) throws ParseException {
        _creditCardId = creditCardId;
        SOURCE.put("credit_card_id", creditCardId);
    }

    public String getCreditCardId() {
        try {
            if (_creditCardId == null && SOURCE.has("credit_card_id") && SOURCE.get("credit_card_id") != null)
                _creditCardId = SOURCE.getString("credit_card_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _creditCardId;
    }

    public FundTransaction creditCardId(String creditCardId) throws ParseException {
        _creditCardId = creditCardId;
        SOURCE.put("credit_card_id", creditCardId);
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public FundTransaction description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setExternalId(String externalId) throws ParseException {
        _externalId = externalId;
        SOURCE.put("external_id", externalId);
    }

    public String getExternalId() {
        try {
            if (_externalId == null && SOURCE.has("external_id") && SOURCE.get("external_id") != null)
                _externalId = SOURCE.getString("external_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _externalId;
    }

    public FundTransaction externalId(String externalId) throws ParseException {
        _externalId = externalId;
        SOURCE.put("external_id", externalId);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public FundTransaction id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setNote(String note) throws ParseException {
        _note = note;
        SOURCE.put("note", note);
    }

    public String getNote() {
        try {
            if (_note == null && SOURCE.has("note") && SOURCE.get("note") != null)
                _note = SOURCE.getString("note");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _note;
    }

    public FundTransaction note(String note) throws ParseException {
        _note = note;
        SOURCE.put("note", note);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public FundTransaction status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setStatusReason(String statusReason) throws ParseException {
        _statusReason = statusReason;
        SOURCE.put("status_reason", statusReason);
    }

    public String getStatusReason() {
        try {
            if (_statusReason == null && SOURCE.has("status_reason") && SOURCE.get("status_reason") != null)
                _statusReason = SOURCE.getString("status_reason");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusReason;
    }

    public FundTransaction statusReason(String statusReason) throws ParseException {
        _statusReason = statusReason;
        SOURCE.put("status_reason", statusReason);
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

    public FundTransaction type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    public void setUpdated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
    }

    public Date getUpdated() {
        try {
            if (_updated == null && SOURCE.has("updated") && SOURCE.get("updated") != null)
                _updated = Date.fromJson(SOURCE.getJsonObject("updated"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_updated != null && _updated.isSet())
        return _updated;

        return null;
    }

    public FundTransaction updated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
        return this;
    }

    public void setWorkOrder(WorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
    }

    public WorkOrder getWorkOrder() {
        try {
            if (_workOrder == null && SOURCE.has("work_order") && SOURCE.get("work_order") != null)
                _workOrder = WorkOrder.fromJson(SOURCE.getJsonObject("work_order"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workOrder != null && _workOrder.isSet())
        return _workOrder;

        return null;
    }

    public FundTransaction workOrder(WorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "completed")
        COMPLETED("completed"),
        @Json(name = "failed")
        FAILED("failed"),
        @Json(name = "holding")
        HOLDING("holding"),
        @Json(name = "pending")
        PENDING("pending");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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

    public enum TypeEnum {
        @Json(name = "ccauth")
        CCAUTH("ccauth"),
        @Json(name = "deposit")
        DEPOSIT("deposit"),
        @Json(name = "insurance_fee")
        INSURANCE_FEE("insurance_fee"),
        @Json(name = "loan")
        LOAN("loan"),
        @Json(name = "payment")
        PAYMENT("payment"),
        @Json(name = "service_fee")
        SERVICE_FEE("service_fee"),
        @Json(name = "withdrawal")
        WITHDRAWAL("withdrawal");

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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(FundTransaction[] array) {
        JsonArray list = new JsonArray();
        for (FundTransaction item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static FundTransaction[] fromJsonArray(JsonArray array) {
        FundTransaction[] list = new FundTransaction[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static FundTransaction fromJson(JsonObject obj) {
        try {
            return new FundTransaction(obj);
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
    public static final Parcelable.Creator<FundTransaction> CREATOR = new Parcelable.Creator<FundTransaction>() {

        @Override
        public FundTransaction createFromParcel(Parcel source) {
            try {
                return FundTransaction.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public FundTransaction[] newArray(int size) {
            return new FundTransaction[size];
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

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}

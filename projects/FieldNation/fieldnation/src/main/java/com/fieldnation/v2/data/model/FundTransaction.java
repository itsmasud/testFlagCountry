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

public class FundTransaction implements Parcelable {
    private static final String TAG = "FundTransaction";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "billing_address")
    private BillingAddress _billingAddress;

    @Json(name = "created")
    private Date _created;

    @Json(name = "credit_card")
    private CreditCard _creditCard;

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
    private JsonObject SOURCE = new JsonObject();

    public FundTransaction() {
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        return _amount;
    }

    public FundTransaction amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setBillingAddress(BillingAddress billingAddress) throws ParseException {
        _billingAddress = billingAddress;
        SOURCE.put("billing_address", billingAddress.getJson());
    }

    public BillingAddress getBillingAddress() {
        return _billingAddress;
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
        return _created;
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
        return _creditCard;
    }

    public FundTransaction creditCard(CreditCard creditCard) throws ParseException {
        _creditCard = creditCard;
        SOURCE.put("credit_card", creditCard.getJson());
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
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
        return _updated;
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
        return _workOrder;
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
            return Unserializer.unserializeObject(FundTransaction.class, obj);
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
}

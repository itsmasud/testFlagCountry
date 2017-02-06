package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class FundTransaction implements Parcelable {
    private static final String TAG = "FundTransaction";

    @Json(name = "note")
    private String _note;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "created")
    private Date _created;

    @Json(name = "description")
    private String _description;

    @Json(name = "external_id")
    private String _externalId;

    @Json(name = "billing_address")
    private BillingAddress _billingAddress;

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "credit_card")
    private CreditCard _creditCard;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "status_reason")
    private String _statusReason;

    @Json(name = "updated")
    private Date _updated;

    @Json(name = "work_order")
    private WorkOrder _workOrder;

    @Json(name = "status")
    private StatusEnum _status;

    public FundTransaction() {
    }

    public void setNote(String note) {
        _note = note;
    }

    public String getNote() {
        return _note;
    }

    public FundTransaction note(String note) {
        _note = note;
        return this;
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public FundTransaction amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public FundTransaction created(Date created) {
        _created = created;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public FundTransaction description(String description) {
        _description = description;
        return this;
    }

    public void setExternalId(String externalId) {
        _externalId = externalId;
    }

    public String getExternalId() {
        return _externalId;
    }

    public FundTransaction externalId(String externalId) {
        _externalId = externalId;
        return this;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        _billingAddress = billingAddress;
    }

    public BillingAddress getBillingAddress() {
        return _billingAddress;
    }

    public FundTransaction billingAddress(BillingAddress billingAddress) {
        _billingAddress = billingAddress;
        return this;
    }

    public void setType(TypeEnum type) {
        _type = type;
    }

    public TypeEnum getType() {
        return _type;
    }

    public FundTransaction type(TypeEnum type) {
        _type = type;
        return this;
    }

    public void setCreditCard(CreditCard creditCard) {
        _creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return _creditCard;
    }

    public FundTransaction creditCard(CreditCard creditCard) {
        _creditCard = creditCard;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public FundTransaction id(Integer id) {
        _id = id;
        return this;
    }

    public void setStatusReason(String statusReason) {
        _statusReason = statusReason;
    }

    public String getStatusReason() {
        return _statusReason;
    }

    public FundTransaction statusReason(String statusReason) {
        _statusReason = statusReason;
        return this;
    }

    public void setUpdated(Date updated) {
        _updated = updated;
    }

    public Date getUpdated() {
        return _updated;
    }

    public FundTransaction updated(Date updated) {
        _updated = updated;
        return this;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
    }

    public WorkOrder getWorkOrder() {
        return _workOrder;
    }

    public FundTransaction workOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public FundTransaction status(StatusEnum status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(FundTransaction fundTransaction) {
        try {
            return Serializer.serializeObject(fundTransaction);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

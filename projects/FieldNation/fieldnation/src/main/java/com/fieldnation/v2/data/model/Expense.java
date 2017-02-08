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

public class Expense implements Parcelable {
    private static final String TAG = "Expense";

    @Json(name = "reason")
    private String _reason;

    @Json(name = "status_description")
    private String _statusDescription;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "quantity")
    private Integer _quantity;

    @Json(name = "added")
    private Date _added;

    @Json(name = "author")
    private User _author;

    @Json(name = "description")
    private String _description;

    @Json(name = "category")
    private ExpenseCategory _category;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "company_expense")
    private ExpenseCompanyExpense _companyExpense;

    public Expense() {
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public Expense reason(String reason) {
        _reason = reason;
        return this;
    }

    public void setStatusDescription(String statusDescription) {
        _statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public Expense statusDescription(String statusDescription) {
        _statusDescription = statusDescription;
        return this;
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public Expense amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setQuantity(Integer quantity) {
        _quantity = quantity;
    }

    public Integer getQuantity() {
        return _quantity;
    }

    public Expense quantity(Integer quantity) {
        _quantity = quantity;
        return this;
    }

    public void setAdded(Date added) {
        _added = added;
    }

    public Date getAdded() {
        return _added;
    }

    public Expense added(Date added) {
        _added = added;
        return this;
    }

    public void setAuthor(User author) {
        _author = author;
    }

    public User getAuthor() {
        return _author;
    }

    public Expense author(User author) {
        _author = author;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public Expense description(String description) {
        _description = description;
        return this;
    }

    public void setCategory(ExpenseCategory category) {
        _category = category;
    }

    public ExpenseCategory getCategory() {
        return _category;
    }

    public Expense category(ExpenseCategory category) {
        _category = category;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public Expense status(StatusEnum status) {
        _status = status;
        return this;
    }

    public void setCompanyExpense(ExpenseCompanyExpense companyExpense) {
        _companyExpense = companyExpense;
    }

    public ExpenseCompanyExpense getCompanyExpense() {
        return _companyExpense;
    }

    public Expense companyExpense(ExpenseCompanyExpense companyExpense) {
        _companyExpense = companyExpense;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "new")
        NEW("new"),
        @Json(name = "approved")
        APPROVED("approved"),
        @Json(name = "disapproved")
        DISAPPROVED("disapproved");

        private String value;

        StatusEnum(String value) {
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
    public static Expense[] fromJsonArray(JsonArray array) {
        Expense[] list = new Expense[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Expense fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Expense.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Expense expense) {
        try {
            return Serializer.serializeObject(expense);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {

        @Override
        public Expense createFromParcel(Parcel source) {
            try {
                return Expense.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
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

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Expense {
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

    public String getReason() {
        return _reason;
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public Double getAmount() {
        return _amount;
    }

    public Integer getQuantity() {
        return _quantity;
    }

    public Date getAdded() {
        return _added;
    }

    public User getAuthor() {
        return _author;
    }

    public String getDescription() {
        return _description;
    }

    public ExpenseCategory getCategory() {
        return _category;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public ExpenseCompanyExpense getCompanyExpense() {
        return _companyExpense;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Expense {
    private static final String TAG = "Expense";

    @Json(name = "reason")
    private String reason;

    @Json(name = "status_description")
    private String statusDescription;

    @Json(name = "amount")
    private Double amount;

    @Json(name = "quantity")
    private Integer quantity;

    @Json(name = "added")
    private Date added;

    @Json(name = "author")
    private User author;

    @Json(name = "description")
    private String description;

    @Json(name = "category")
    private ExpenseCategory category;

    @Json(name = "status")
    private StatusEnum status;

    @Json(name = "company_expense")
    private ExpenseCompanyExpense companyExpense;

    public Expense() {
    }

    public String getReason() {
        return reason;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getAdded() {
        return added;
    }

    public User getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public ExpenseCompanyExpense getCompanyExpense() {
        return companyExpense;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Expense fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Expense.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

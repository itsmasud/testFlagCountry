package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Expense {
    private static final String TAG = "Expense";

    @Json(name = "description")
    private String description = null;

    @Json(name = "quantity")
    private Integer quantity = null;

    @Json(name = "status")
    private StatusEnum status = null;

    @Json(name = "status_description")
    private String statusDescription = null;

    @Json(name = "category")
    private UserBy category = null;

    @Json(name = "added")
    private String added = null;

    @Json(name = "author")
    private User author = null;

    @Json(name = "company_expense")
    private ExpenseCompanyExpense companyExpense = null;

    @Json(name = "amount")
    private Double amount = null;

    @Json(name = "reason")
    private String reason = null;

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

    public Expense() {
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public UserBy getCategory() {
        return category;
    }

    public String getAdded() {
        return added;
    }

    public User getAuthor() {
        return author;
    }

    public ExpenseCompanyExpense getCompanyExpense() {
        return companyExpense;
    }

    public Double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
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


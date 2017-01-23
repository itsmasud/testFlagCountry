package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ExpenseCompanyExpense {
    private static final String TAG = "ExpenseCompanyExpense";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "expense_amount")
    private Double expenseAmount = null;

    @Json(name = "hidden_tags")
    private String hiddenTags = null;

    @Json(name = "api_code")
    private String apiCode = null;

    public ExpenseCompanyExpense() {
    }

    public Integer getId() {
        return id;
    }

    public Double getExpenseAmount() {
        return expenseAmount;
    }

    public String getHiddenTags() {
        return hiddenTags;
    }

    public String getApiCode() {
        return apiCode;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ExpenseCompanyExpense fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ExpenseCompanyExpense.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ExpenseCompanyExpense expenseCompanyExpense) {
        try {
            return Serializer.serializeObject(expenseCompanyExpense);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


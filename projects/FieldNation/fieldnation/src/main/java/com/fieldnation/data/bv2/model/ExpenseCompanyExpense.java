package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ExpenseCompanyExpense {
    private static final String TAG = "ExpenseCompanyExpense";

    @Json(name = "api_code")
    private String _apiCode;

    @Json(name = "expense_amount")
    private Double _expenseAmount;

    @Json(name = "hidden_tags")
    private String _hiddenTags;

    @Json(name = "id")
    private Integer _id;

    public ExpenseCompanyExpense() {
    }

    public String getApiCode() {
        return _apiCode;
    }

    public Double getExpenseAmount() {
        return _expenseAmount;
    }

    public String getHiddenTags() {
        return _hiddenTags;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ExpenseCompanyExpense fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ExpenseCompanyExpense.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

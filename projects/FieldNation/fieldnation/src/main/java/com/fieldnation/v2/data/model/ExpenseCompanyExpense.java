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

public class ExpenseCompanyExpense implements Parcelable {
    private static final String TAG = "ExpenseCompanyExpense";

    @Json(name = "api_code")
    private String _apiCode;

    @Json(name = "expense_amount")
    private Double _expenseAmount;

    @Json(name = "hidden_tags")
    private String _hiddenTags;

    @Json(name = "id")
    private Integer _id;

    @Source
    private JsonObject SOURCE;

    public ExpenseCompanyExpense() {
        SOURCE = new JsonObject();
    }

    public ExpenseCompanyExpense(JsonObject obj) {
        SOURCE = obj;
    }

    public void setApiCode(String apiCode) throws ParseException {
        _apiCode = apiCode;
        SOURCE.put("api_code", apiCode);
    }

    public String getApiCode() {
        try {
            if (_apiCode == null && SOURCE.has("api_code") && SOURCE.get("api_code") != null)
                _apiCode = SOURCE.getString("api_code");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _apiCode;
    }

    public ExpenseCompanyExpense apiCode(String apiCode) throws ParseException {
        _apiCode = apiCode;
        SOURCE.put("api_code", apiCode);
        return this;
    }

    public void setExpenseAmount(Double expenseAmount) throws ParseException {
        _expenseAmount = expenseAmount;
        SOURCE.put("expense_amount", expenseAmount);
    }

    public Double getExpenseAmount() {
        try {
            if (_expenseAmount == null && SOURCE.has("expense_amount") && SOURCE.get("expense_amount") != null)
                _expenseAmount = SOURCE.getDouble("expense_amount");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expenseAmount;
    }

    public ExpenseCompanyExpense expenseAmount(Double expenseAmount) throws ParseException {
        _expenseAmount = expenseAmount;
        SOURCE.put("expense_amount", expenseAmount);
        return this;
    }

    public void setHiddenTags(String hiddenTags) throws ParseException {
        _hiddenTags = hiddenTags;
        SOURCE.put("hidden_tags", hiddenTags);
    }

    public String getHiddenTags() {
        try {
            if (_hiddenTags == null && SOURCE.has("hidden_tags") && SOURCE.get("hidden_tags") != null)
                _hiddenTags = SOURCE.getString("hidden_tags");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hiddenTags;
    }

    public ExpenseCompanyExpense hiddenTags(String hiddenTags) throws ParseException {
        _hiddenTags = hiddenTags;
        SOURCE.put("hidden_tags", hiddenTags);
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

    public ExpenseCompanyExpense id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ExpenseCompanyExpense[] array) {
        JsonArray list = new JsonArray();
        for (ExpenseCompanyExpense item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ExpenseCompanyExpense[] fromJsonArray(JsonArray array) {
        ExpenseCompanyExpense[] list = new ExpenseCompanyExpense[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ExpenseCompanyExpense fromJson(JsonObject obj) {
        try {
            return new ExpenseCompanyExpense(obj);
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
    public static final Parcelable.Creator<ExpenseCompanyExpense> CREATOR = new Parcelable.Creator<ExpenseCompanyExpense>() {

        @Override
        public ExpenseCompanyExpense createFromParcel(Parcel source) {
            try {
                return ExpenseCompanyExpense.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ExpenseCompanyExpense[] newArray(int size) {
            return new ExpenseCompanyExpense[size];
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

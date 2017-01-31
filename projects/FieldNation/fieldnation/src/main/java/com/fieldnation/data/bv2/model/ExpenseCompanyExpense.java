package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
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

    public ExpenseCompanyExpense() {
    }

    public void setApiCode(String apiCode) {
        _apiCode = apiCode;
    }

    public String getApiCode() {
        return _apiCode;
    }

    public ExpenseCompanyExpense apiCode(String apiCode) {
        _apiCode = apiCode;
        return this;
    }

    public void setExpenseAmount(Double expenseAmount) {
        _expenseAmount = expenseAmount;
    }

    public Double getExpenseAmount() {
        return _expenseAmount;
    }

    public ExpenseCompanyExpense expenseAmount(Double expenseAmount) {
        _expenseAmount = expenseAmount;
        return this;
    }

    public void setHiddenTags(String hiddenTags) {
        _hiddenTags = hiddenTags;
    }

    public String getHiddenTags() {
        return _hiddenTags;
    }

    public ExpenseCompanyExpense hiddenTags(String hiddenTags) {
        _hiddenTags = hiddenTags;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public ExpenseCompanyExpense id(Integer id) {
        _id = id;
        return this;
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
        dest.writeParcelable(toJson(), flags);
    }
}

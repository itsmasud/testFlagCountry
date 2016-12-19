package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Expense implements Parcelable {
    private static final String TAG = "Expense";

    @Json(name = "approved")
    private Boolean _approved;
    @Json(name = "categoryId")
    private Integer _categoryId;
    @Json(name = "description")
    private String _description;
    @Json(name = "expenseId")
    private Integer _expenseId;
    @Json(name = "price")
    private Double _price;
    @Json(name = "status")
    private String _status;
    @Json(name = "statusDescription")
    private String _statusDescription;

    public Expense() {
    }

    public Boolean getApproved() {
        return _approved;
    }

    public Integer getCategoryId() {
        return _categoryId;
    }

    public String getDescription() {
        return _description;
    }

    public Integer getExpenseId() {
        return _expenseId;
    }

    public Double getPrice() {
        return _price;
    }

    public String getStatus() {
        return _status;
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Expense additionalExpense) {
        try {
            return Serializer.serializeObject(additionalExpense);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Expense fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Expense.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/
    public Expense(String description, double amount, ExpenseCategory category) {
        if (category != null) {
            _categoryId = category.getId();
        } else {
            _categoryId = null;
        }
        _description = description;
        _price = amount;
        // Todo, localize this?
        _status = "New";
    }

	/*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {

        @Override
        public Expense createFromParcel(Parcel source) {
            try {
                return Expense.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
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

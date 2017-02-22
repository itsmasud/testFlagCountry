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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Expense implements Parcelable {
    private static final String TAG = "Expense";

    @Json(name = "added")
    private Date _added;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "author")
    private User _author;

    @Json(name = "category")
    private ExpenseCategory _category;

    @Json(name = "company_expense")
    private ExpenseCompanyExpense _companyExpense;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "quantity")
    private Integer _quantity;

    @Json(name = "reason")
    private String _reason;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "status_description")
    private String _statusDescription;

    @Source
    private JsonObject SOURCE;

    public Expense() {
        SOURCE = new JsonObject();
    }

    public Expense(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAdded(Date added) throws ParseException {
        _added = added;
        SOURCE.put("added", added.getJson());
    }

    public Date getAdded() {
        try {
            if (_added != null)
                return _added;

            if (SOURCE.has("added") && SOURCE.get("added") != null)
                _added = Date.fromJson(SOURCE.getJsonObject("added"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _added;
    }

    public Expense added(Date added) throws ParseException {
        _added = added;
        SOURCE.put("added", added.getJson());
        return this;
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        try {
            if (_amount != null)
                return _amount;

            if (SOURCE.has("amount") && SOURCE.get("amount") != null)
                _amount = SOURCE.getDouble("amount");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _amount;
    }

    public Expense amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author != null)
                return _author;

            if (SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _author;
    }

    public Expense author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setCategory(ExpenseCategory category) throws ParseException {
        _category = category;
        SOURCE.put("category", category.getJson());
    }

    public ExpenseCategory getCategory() {
        try {
            if (_category != null)
                return _category;

            if (SOURCE.has("category") && SOURCE.get("category") != null)
                _category = ExpenseCategory.fromJson(SOURCE.getJsonObject("category"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _category;
    }

    public Expense category(ExpenseCategory category) throws ParseException {
        _category = category;
        SOURCE.put("category", category.getJson());
        return this;
    }

    public void setCompanyExpense(ExpenseCompanyExpense companyExpense) throws ParseException {
        _companyExpense = companyExpense;
        SOURCE.put("company_expense", companyExpense.getJson());
    }

    public ExpenseCompanyExpense getCompanyExpense() {
        try {
            if (_companyExpense != null)
                return _companyExpense;

            if (SOURCE.has("company_expense") && SOURCE.get("company_expense") != null)
                _companyExpense = ExpenseCompanyExpense.fromJson(SOURCE.getJsonObject("company_expense"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyExpense;
    }

    public Expense companyExpense(ExpenseCompanyExpense companyExpense) throws ParseException {
        _companyExpense = companyExpense;
        SOURCE.put("company_expense", companyExpense.getJson());
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description != null)
                return _description;

            if (SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public Expense description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Expense id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setQuantity(Integer quantity) throws ParseException {
        _quantity = quantity;
        SOURCE.put("quantity", quantity);
    }

    public Integer getQuantity() {
        try {
            if (_quantity != null)
                return _quantity;

            if (SOURCE.has("quantity") && SOURCE.get("quantity") != null)
                _quantity = SOURCE.getInt("quantity");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _quantity;
    }

    public Expense quantity(Integer quantity) throws ParseException {
        _quantity = quantity;
        SOURCE.put("quantity", quantity);
        return this;
    }

    public void setReason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
    }

    public String getReason() {
        try {
            if (_reason != null)
                return _reason;

            if (SOURCE.has("reason") && SOURCE.get("reason") != null)
                _reason = SOURCE.getString("reason");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reason;
    }

    public Expense reason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status != null)
                return _status;

            if (SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public Expense status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setStatusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
    }

    public String getStatusDescription() {
        try {
            if (_statusDescription != null)
                return _statusDescription;

            if (SOURCE.has("status_description") && SOURCE.get("status_description") != null)
                _statusDescription = SOURCE.getString("status_description");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusDescription;
    }

    public Expense statusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "approved")
        APPROVED("approved"),
        @Json(name = "disapproved")
        DISAPPROVED("disapproved"),
        @Json(name = "new")
        NEW("new");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Expense[] array) {
        JsonArray list = new JsonArray();
        for (Expense item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Expense[] fromJsonArray(JsonArray array) {
        Expense[] list = new Expense[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Expense fromJson(JsonObject obj) {
        try {
            return new Expense(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

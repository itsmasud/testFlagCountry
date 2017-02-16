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
    private JsonObject SOURCE = new JsonObject();

    public Expense() {
    }

    public void setAdded(Date added) throws ParseException {
        _added = added;
        SOURCE.put("added", added.getJson());
    }

    public Date getAdded() {
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
            return Unserializer.unserializeObject(Expense.class, obj);
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

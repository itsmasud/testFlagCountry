package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ExpenseCategory {
    private static final String TAG = "ExpenseCategory";

    @Json(name = "id")
    private Integer _id;
    @Json(name = "name")
    private String _name;

    public ExpenseCategory() {
    }

    public Integer getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ExpenseCategory expenseCategory) {
        try {
            return Serializer.serializeObject(expenseCategory);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ExpenseCategory fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(ExpenseCategory.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/
    @Override
    public String toString() {
        return getName();
    }
}

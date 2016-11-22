package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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
            return Unserializer.unserializeObject(ExpenseCategory.class, json);
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

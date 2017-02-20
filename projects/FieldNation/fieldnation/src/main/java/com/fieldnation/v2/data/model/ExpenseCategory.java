package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class ExpenseCategory implements Parcelable {
    private static final String TAG = "ExpenseCategory";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ExpenseCategory() {
    }

    public ExpenseCategory(int id, String name) {
        _id = id;
        _name = name;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public ExpenseCategory id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public ExpenseCategory name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ExpenseCategory[] array) {
        JsonArray list = new JsonArray();
        for (ExpenseCategory item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ExpenseCategory[] fromJsonArray(JsonArray array) {
        ExpenseCategory[] list = new ExpenseCategory[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ExpenseCategory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ExpenseCategory.class, obj);
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
    public static final Parcelable.Creator<ExpenseCategory> CREATOR = new Parcelable.Creator<ExpenseCategory>() {

        @Override
        public ExpenseCategory createFromParcel(Parcel source) {
            try {
                return ExpenseCategory.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ExpenseCategory[] newArray(int size) {
            return new ExpenseCategory[size];
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

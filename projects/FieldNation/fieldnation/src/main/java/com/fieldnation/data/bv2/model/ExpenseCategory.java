package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class ExpenseCategory implements Parcelable {
    private static final String TAG = "ExpenseCategory";

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public ExpenseCategory() {
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public ExpenseCategory name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public ExpenseCategory id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ExpenseCategory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ExpenseCategory.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ExpenseCategory expenseCategory) {
        try {
            return Serializer.serializeObject(expenseCategory);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

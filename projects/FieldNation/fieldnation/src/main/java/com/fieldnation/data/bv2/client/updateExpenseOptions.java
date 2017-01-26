package com.fieldnation.data.bv2.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.data.bv2.model.Expense;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class updateExpenseOptions implements Parcelable {
    private static final String TAG = "updateExpenseOptions";

    @Json(name = "async")
    private Boolean _async;

    @Json(name = "expense")
    private Expense _expense;

    public updateExpenseOptions() {
    }

    public void setAsync(Boolean async) {
        _async = async;
    }

    public Boolean getAsync() {
        return _async;
    }

    public updateExpenseOptions async(Boolean async) {
        _async = async;
        return this;
    }

    public void setExpense(Expense expense) {
        _expense = expense;
    }

    public Expense getExpense() {
        return _expense;
    }

    public updateExpenseOptions expense(Expense expense) {
        _expense = expense;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static updateExpenseOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(updateExpenseOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(updateExpenseOptions updateExpenseOptions) {
        try {
            return Serializer.serializeObject(updateExpenseOptions);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<updateExpenseOptions> CREATOR = new Parcelable.Creator<updateExpenseOptions>() {

        @Override
        public updateExpenseOptions createFromParcel(Parcel source) {
            try {
                return updateExpenseOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public updateExpenseOptions[] newArray(int size) {
            return new updateExpenseOptions[size];
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

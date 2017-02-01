package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.v2.data.model.*;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class UpdateExpenseOptions implements Parcelable {
    private static final String TAG = "UpdateExpenseOptions";

    @Json(name = "async")
    private Boolean _async;

    @Json(name = "expense")
    private Expense _expense;

    public UpdateExpenseOptions() {
    }

    public void setAsync(Boolean async) {
        _async = async;
    }

    public Boolean getAsync() {
        return _async;
    }

    public UpdateExpenseOptions async(Boolean async) {
        _async = async;
        return this;
    }

    public void setExpense(Expense expense) {
        _expense = expense;
    }

    public Expense getExpense() {
        return _expense;
    }

    public UpdateExpenseOptions expense(Expense expense) {
        _expense = expense;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateExpenseOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateExpenseOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateExpenseOptions updateExpenseOptions) {
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
    public static final Parcelable.Creator<UpdateExpenseOptions> CREATOR = new Parcelable.Creator<UpdateExpenseOptions>() {

        @Override
        public UpdateExpenseOptions createFromParcel(Parcel source) {
            try {
                return UpdateExpenseOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateExpenseOptions[] newArray(int size) {
            return new UpdateExpenseOptions[size];
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

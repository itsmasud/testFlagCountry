package com.fieldnation.v2.data.listener;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 1/27/17.
 */

public class TransactionParams implements Parcelable {
    private static final String TAG = "TransactionParams";

    @Json(name = "topicId")
    public String topicId;

    @Json(name = "apiClass")
    public String apiClassName;

    @Json(name = "apiFunction")
    public String apiFunction;

    @Json(name = "methodParams")
    public JsonObject methodParams;

    public TransactionParams() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TransactionParams fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TransactionParams.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TransactionParams transactionParams) {
        try {
            return Serializer.serializeObject(transactionParams);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<TransactionParams> CREATOR = new Parcelable.Creator<TransactionParams>() {

        @Override
        public TransactionParams createFromParcel(Parcel source) {
            try {
                return TransactionParams.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TransactionParams[] newArray(int size) {
            return new TransactionParams[size];
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

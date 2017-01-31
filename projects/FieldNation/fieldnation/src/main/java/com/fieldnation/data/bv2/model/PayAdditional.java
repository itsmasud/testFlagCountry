package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class PayAdditional implements Parcelable {
    private static final String TAG = "PayAdditional";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "units")
    private Double _units;

    public PayAdditional() {
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public PayAdditional amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setUnits(Double units) {
        _units = units;
    }

    public Double getUnits() {
        return _units;
    }

    public PayAdditional units(Double units) {
        _units = units;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayAdditional[] fromJsonArray(JsonArray array) {
        PayAdditional[] list = new PayAdditional[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayAdditional fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayAdditional.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayAdditional payAdditional) {
        try {
            return Serializer.serializeObject(payAdditional);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayAdditional> CREATOR = new Parcelable.Creator<PayAdditional>() {

        @Override
        public PayAdditional createFromParcel(Parcel source) {
            try {
                return PayAdditional.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayAdditional[] newArray(int size) {
            return new PayAdditional[size];
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

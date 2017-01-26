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

public class PayBase implements Parcelable {
    private static final String TAG = "PayBase";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "units")
    private Double _units;

    public PayBase() {
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public PayBase amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setUnits(Double units) {
        _units = units;
    }

    public Double getUnits() {
        return _units;
    }

    public PayBase units(Double units) {
        _units = units;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayBase fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayBase.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayBase payBase) {
        try {
            return Serializer.serializeObject(payBase);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayBase> CREATOR = new Parcelable.Creator<PayBase>() {

        @Override
        public PayBase createFromParcel(Parcel source) {
            try {
                return PayBase.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayBase[] newArray(int size) {
            return new PayBase[size];
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

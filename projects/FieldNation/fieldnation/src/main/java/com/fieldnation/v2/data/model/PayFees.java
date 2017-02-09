package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class PayFees implements Parcelable {
    private static final String TAG = "PayFees";

    @Json(name = "buyer")
    private Fee _buyer;

    @Json(name = "cancellation")
    private Fee _cancellation;

    @Json(name = "flat")
    private Fee _flat;

    @Json(name = "insurance")
    private Fee _insurance;

    @Json(name = "provider")
    private Fee _provider;

    public PayFees() {
    }

    public void setBuyer(Fee buyer) {
        _buyer = buyer;
    }

    public Fee getBuyer() {
        return _buyer;
    }

    public PayFees buyer(Fee buyer) {
        _buyer = buyer;
        return this;
    }

    public void setCancellation(Fee cancellation) {
        _cancellation = cancellation;
    }

    public Fee getCancellation() {
        return _cancellation;
    }

    public PayFees cancellation(Fee cancellation) {
        _cancellation = cancellation;
        return this;
    }

    public void setFlat(Fee flat) {
        _flat = flat;
    }

    public Fee getFlat() {
        return _flat;
    }

    public PayFees flat(Fee flat) {
        _flat = flat;
        return this;
    }

    public void setInsurance(Fee insurance) {
        _insurance = insurance;
    }

    public Fee getInsurance() {
        return _insurance;
    }

    public PayFees insurance(Fee insurance) {
        _insurance = insurance;
        return this;
    }

    public void setProvider(Fee provider) {
        _provider = provider;
    }

    public Fee getProvider() {
        return _provider;
    }

    public PayFees provider(Fee provider) {
        _provider = provider;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayFees[] fromJsonArray(JsonArray array) {
        PayFees[] list = new PayFees[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayFees fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayFees.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayFees payFees) {
        try {
            return Serializer.serializeObject(payFees);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayFees> CREATOR = new Parcelable.Creator<PayFees>() {

        @Override
        public PayFees createFromParcel(Parcel source) {
            try {
                return PayFees.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayFees[] newArray(int size) {
            return new PayFees[size];
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

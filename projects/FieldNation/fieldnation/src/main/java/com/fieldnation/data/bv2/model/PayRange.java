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

public class PayRange implements Parcelable {
    private static final String TAG = "PayRange";

    @Json(name = "min")
    private Double _min;

    @Json(name = "max")
    private Double _max;

    public PayRange() {
    }

    public void setMin(Double min) {
        _min = min;
    }

    public Double getMin() {
        return _min;
    }

    public PayRange min(Double min) {
        _min = min;
        return this;
    }

    public void setMax(Double max) {
        _max = max;
    }

    public Double getMax() {
        return _max;
    }

    public PayRange max(Double max) {
        _max = max;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayRange fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayRange.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayRange payRange) {
        try {
            return Serializer.serializeObject(payRange);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayRange> CREATOR = new Parcelable.Creator<PayRange>() {

        @Override
        public PayRange createFromParcel(Parcel source) {
            try {
                return PayRange.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayRange[] newArray(int size) {
            return new PayRange[size];
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

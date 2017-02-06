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

public class PayModifiersSum implements Parcelable {
    private static final String TAG = "PayModifiersSum";

    @Json(name = "all")
    private Double _all;

    @Json(name = "uncharged")
    private Double _uncharged;

    @Json(name = "charged")
    private Double _charged;

    public PayModifiersSum() {
    }

    public void setAll(Double all) {
        _all = all;
    }

    public Double getAll() {
        return _all;
    }

    public PayModifiersSum all(Double all) {
        _all = all;
        return this;
    }

    public void setUncharged(Double uncharged) {
        _uncharged = uncharged;
    }

    public Double getUncharged() {
        return _uncharged;
    }

    public PayModifiersSum uncharged(Double uncharged) {
        _uncharged = uncharged;
        return this;
    }

    public void setCharged(Double charged) {
        _charged = charged;
    }

    public Double getCharged() {
        return _charged;
    }

    public PayModifiersSum charged(Double charged) {
        _charged = charged;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiersSum[] fromJsonArray(JsonArray array) {
        PayModifiersSum[] list = new PayModifiersSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayModifiersSum fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiersSum.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifiersSum payModifiersSum) {
        try {
            return Serializer.serializeObject(payModifiersSum);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayModifiersSum> CREATOR = new Parcelable.Creator<PayModifiersSum>() {

        @Override
        public PayModifiersSum createFromParcel(Parcel source) {
            try {
                return PayModifiersSum.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayModifiersSum[] newArray(int size) {
            return new PayModifiersSum[size];
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

package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class PayFees implements Parcelable {
    private static final String TAG = "PayFees";

    @Json(name = "buyer")
    private PayModifier _buyer;

    @Json(name = "cancellation")
    private PayModifier _cancellation;

    @Json(name = "flat")
    private PayModifier _flat;

    @Json(name = "insurance")
    private PayModifier _insurance;

    @Json(name = "provider")
    private PayModifier _provider;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayFees() {
    }

    public void setBuyer(PayModifier buyer) throws ParseException {
        _buyer = buyer;
        SOURCE.put("buyer", buyer.getJson());
    }

    public PayModifier getBuyer() {
        return _buyer;
    }

    public PayFees buyer(PayModifier buyer) throws ParseException {
        _buyer = buyer;
        SOURCE.put("buyer", buyer.getJson());
        return this;
    }

    public void setCancellation(PayModifier cancellation) throws ParseException {
        _cancellation = cancellation;
        SOURCE.put("cancellation", cancellation.getJson());
    }

    public PayModifier getCancellation() {
        return _cancellation;
    }

    public PayFees cancellation(PayModifier cancellation) throws ParseException {
        _cancellation = cancellation;
        SOURCE.put("cancellation", cancellation.getJson());
        return this;
    }

    public void setFlat(PayModifier flat) throws ParseException {
        _flat = flat;
        SOURCE.put("flat", flat.getJson());
    }

    public PayModifier getFlat() {
        return _flat;
    }

    public PayFees flat(PayModifier flat) throws ParseException {
        _flat = flat;
        SOURCE.put("flat", flat.getJson());
        return this;
    }

    public void setInsurance(PayModifier insurance) throws ParseException {
        _insurance = insurance;
        SOURCE.put("insurance", insurance.getJson());
    }

    public PayModifier getInsurance() {
        return _insurance;
    }

    public PayFees insurance(PayModifier insurance) throws ParseException {
        _insurance = insurance;
        SOURCE.put("insurance", insurance.getJson());
        return this;
    }

    public void setProvider(PayModifier provider) throws ParseException {
        _provider = provider;
        SOURCE.put("provider", provider.getJson());
    }

    public PayModifier getProvider() {
        return _provider;
    }

    public PayFees provider(PayModifier provider) throws ParseException {
        _provider = provider;
        SOURCE.put("provider", provider.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayFees[] array) {
        JsonArray list = new JsonArray();
        for (PayFees item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

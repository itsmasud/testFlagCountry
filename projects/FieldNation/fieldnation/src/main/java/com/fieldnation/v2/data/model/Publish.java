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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Publish implements Parcelable {
    private static final String TAG = "Publish";

    @Json(name = "billing_address")
    private BillingAddress _billingAddress;

    @Json(name = "credit_card")
    private CreditCard _creditCard;

    @Json(name = "credit_card_id")
    private String _creditCardId;

    @Source
    private JsonObject SOURCE;

    public Publish() {
        SOURCE = new JsonObject();
    }

    public Publish(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBillingAddress(BillingAddress billingAddress) throws ParseException {
        _billingAddress = billingAddress;
        SOURCE.put("billing_address", billingAddress.getJson());
    }

    public BillingAddress getBillingAddress() {
        try {
            if (_billingAddress == null && SOURCE.has("billing_address") && SOURCE.get("billing_address") != null)
                _billingAddress = BillingAddress.fromJson(SOURCE.getJsonObject("billing_address"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_billingAddress == null)
            _billingAddress = new BillingAddress();

            return _billingAddress;
    }

    public Publish billingAddress(BillingAddress billingAddress) throws ParseException {
        _billingAddress = billingAddress;
        SOURCE.put("billing_address", billingAddress.getJson());
        return this;
    }

    public void setCreditCard(CreditCard creditCard) throws ParseException {
        _creditCard = creditCard;
        SOURCE.put("credit_card", creditCard.getJson());
    }

    public CreditCard getCreditCard() {
        try {
            if (_creditCard == null && SOURCE.has("credit_card") && SOURCE.get("credit_card") != null)
                _creditCard = CreditCard.fromJson(SOURCE.getJsonObject("credit_card"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_creditCard == null)
            _creditCard = new CreditCard();

            return _creditCard;
    }

    public Publish creditCard(CreditCard creditCard) throws ParseException {
        _creditCard = creditCard;
        SOURCE.put("credit_card", creditCard.getJson());
        return this;
    }

    public void setCreditCardId(String creditCardId) throws ParseException {
        _creditCardId = creditCardId;
        SOURCE.put("credit_card_id", creditCardId);
    }

    public String getCreditCardId() {
        try {
            if (_creditCardId == null && SOURCE.has("credit_card_id") && SOURCE.get("credit_card_id") != null)
                _creditCardId = SOURCE.getString("credit_card_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _creditCardId;
    }

    public Publish creditCardId(String creditCardId) throws ParseException {
        _creditCardId = creditCardId;
        SOURCE.put("credit_card_id", creditCardId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Publish[] array) {
        JsonArray list = new JsonArray();
        for (Publish item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Publish[] fromJsonArray(JsonArray array) {
        Publish[] list = new Publish[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Publish fromJson(JsonObject obj) {
        try {
            return new Publish(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Publish> CREATOR = new Parcelable.Creator<Publish>() {

        @Override
        public Publish createFromParcel(Parcel source) {
            try {
                return Publish.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Publish[] newArray(int size) {
            return new Publish[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}

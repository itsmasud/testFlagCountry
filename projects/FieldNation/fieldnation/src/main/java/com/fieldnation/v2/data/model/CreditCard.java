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

public class CreditCard implements Parcelable {
    private static final String TAG = "CreditCard";

    @Json(name = "card_number")
    private String _cardNumber;

    @Json(name = "cardholder_name")
    private String _cardholderName;

    @Json(name = "cvv")
    private String _cvv;

    @Json(name = "exp_date")
    private String _expDate;

    @Json(name = "type")
    private TypeEnum _type;

    public CreditCard() {
    }

    public void setCardNumber(String cardNumber) {
        _cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return _cardNumber;
    }

    public CreditCard cardNumber(String cardNumber) {
        _cardNumber = cardNumber;
        return this;
    }

    public void setCardholderName(String cardholderName) {
        _cardholderName = cardholderName;
    }

    public String getCardholderName() {
        return _cardholderName;
    }

    public CreditCard cardholderName(String cardholderName) {
        _cardholderName = cardholderName;
        return this;
    }

    public void setCvv(String cvv) {
        _cvv = cvv;
    }

    public String getCvv() {
        return _cvv;
    }

    public CreditCard cvv(String cvv) {
        _cvv = cvv;
        return this;
    }

    public void setExpDate(String expDate) {
        _expDate = expDate;
    }

    public String getExpDate() {
        return _expDate;
    }

    public CreditCard expDate(String expDate) {
        _expDate = expDate;
        return this;
    }

    public void setType(TypeEnum type) {
        _type = type;
    }

    public TypeEnum getType() {
        return _type;
    }

    public CreditCard type(TypeEnum type) {
        _type = type;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum TypeEnum {
        @Json(name = "discover")
        DISCOVER("discover"),
        @Json(name = "diners club")
        DINERS_CLUB("diners club"),
        @Json(name = "american express")
        AMERICAN_EXPRESS("american express"),
        @Json(name = "visa")
        VISA("visa"),
        @Json(name = "jcb")
        JCB("jcb"),
        @Json(name = "mastercard")
        MASTERCARD("mastercard");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CreditCard[] fromJsonArray(JsonArray array) {
        CreditCard[] list = new CreditCard[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CreditCard fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CreditCard.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CreditCard creditCard) {
        try {
            return Serializer.serializeObject(creditCard);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CreditCard> CREATOR = new Parcelable.Creator<CreditCard>() {

        @Override
        public CreditCard createFromParcel(Parcel source) {
            try {
                return CreditCard.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CreditCard[] newArray(int size) {
            return new CreditCard[size];
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

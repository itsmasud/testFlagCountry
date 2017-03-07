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

    @Source
    private JsonObject SOURCE;

    public CreditCard() {
        SOURCE = new JsonObject();
    }

    public CreditCard(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCardNumber(String cardNumber) throws ParseException {
        _cardNumber = cardNumber;
        SOURCE.put("card_number", cardNumber);
    }

    public String getCardNumber() {
        try {
            if (_cardNumber != null)
                return _cardNumber;

            if (SOURCE.has("card_number") && SOURCE.get("card_number") != null)
                _cardNumber = SOURCE.getString("card_number");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cardNumber;
    }

    public CreditCard cardNumber(String cardNumber) throws ParseException {
        _cardNumber = cardNumber;
        SOURCE.put("card_number", cardNumber);
        return this;
    }

    public void setCardholderName(String cardholderName) throws ParseException {
        _cardholderName = cardholderName;
        SOURCE.put("cardholder_name", cardholderName);
    }

    public String getCardholderName() {
        try {
            if (_cardholderName != null)
                return _cardholderName;

            if (SOURCE.has("cardholder_name") && SOURCE.get("cardholder_name") != null)
                _cardholderName = SOURCE.getString("cardholder_name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cardholderName;
    }

    public CreditCard cardholderName(String cardholderName) throws ParseException {
        _cardholderName = cardholderName;
        SOURCE.put("cardholder_name", cardholderName);
        return this;
    }

    public void setCvv(String cvv) throws ParseException {
        _cvv = cvv;
        SOURCE.put("cvv", cvv);
    }

    public String getCvv() {
        try {
            if (_cvv != null)
                return _cvv;

            if (SOURCE.has("cvv") && SOURCE.get("cvv") != null)
                _cvv = SOURCE.getString("cvv");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cvv;
    }

    public CreditCard cvv(String cvv) throws ParseException {
        _cvv = cvv;
        SOURCE.put("cvv", cvv);
        return this;
    }

    public void setExpDate(String expDate) throws ParseException {
        _expDate = expDate;
        SOURCE.put("exp_date", expDate);
    }

    public String getExpDate() {
        try {
            if (_expDate != null)
                return _expDate;

            if (SOURCE.has("exp_date") && SOURCE.get("exp_date") != null)
                _expDate = SOURCE.getString("exp_date");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expDate;
    }

    public CreditCard expDate(String expDate) throws ParseException {
        _expDate = expDate;
        SOURCE.put("exp_date", expDate);
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public CreditCard type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum TypeEnum {
        @Json(name = "american express")
        AMERICAN_EXPRESS("american express"),
        @Json(name = "diners club")
        DINERS_CLUB("diners club"),
        @Json(name = "discover")
        DISCOVER("discover"),
        @Json(name = "jcb")
        JCB("jcb"),
        @Json(name = "mastercard")
        MASTERCARD("mastercard"),
        @Json(name = "visa")
        VISA("visa");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CreditCard[] array) {
        JsonArray list = new JsonArray();
        for (CreditCard item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CreditCard[] fromJsonArray(JsonArray array) {
        CreditCard[] list = new CreditCard[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CreditCard fromJson(JsonObject obj) {
        try {
            return new CreditCard(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}

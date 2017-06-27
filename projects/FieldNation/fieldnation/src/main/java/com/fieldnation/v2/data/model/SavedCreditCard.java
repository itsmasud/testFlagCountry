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

public class SavedCreditCard implements Parcelable {
    private static final String TAG = "SavedCreditCard";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "card_id")
    private Integer _cardId;

    @Json(name = "card_type")
    private CardTypeEnum _cardType;

    @Json(name = "expiration_date")
    private String _expirationDate;

    @Json(name = "isExpired")
    private Integer _isExpired;

    @Json(name = "last_four")
    private String _lastFour;

    @Source
    private JsonObject SOURCE;

    public SavedCreditCard() {
        SOURCE = new JsonObject();
    }

    public SavedCreditCard(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new ActionsEnum[0];

        return _actions;
    }

    public SavedCreditCard actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setCardId(Integer cardId) throws ParseException {
        _cardId = cardId;
        SOURCE.put("card_id", cardId);
    }

    public Integer getCardId() {
        try {
            if (_cardId == null && SOURCE.has("card_id") && SOURCE.get("card_id") != null)
                _cardId = SOURCE.getInt("card_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cardId;
    }

    public SavedCreditCard cardId(Integer cardId) throws ParseException {
        _cardId = cardId;
        SOURCE.put("card_id", cardId);
        return this;
    }

    public void setCardType(CardTypeEnum cardType) throws ParseException {
        _cardType = cardType;
        SOURCE.put("card_type", cardType.toString());
    }

    public CardTypeEnum getCardType() {
        try {
            if (_cardType == null && SOURCE.has("card_type") && SOURCE.get("card_type") != null)
                _cardType = CardTypeEnum.fromString(SOURCE.getString("card_type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cardType;
    }

    public SavedCreditCard cardType(CardTypeEnum cardType) throws ParseException {
        _cardType = cardType;
        SOURCE.put("card_type", cardType.toString());
        return this;
    }

    public void setExpirationDate(String expirationDate) throws ParseException {
        _expirationDate = expirationDate;
        SOURCE.put("expiration_date", expirationDate);
    }

    public String getExpirationDate() {
        try {
            if (_expirationDate == null && SOURCE.has("expiration_date") && SOURCE.get("expiration_date") != null)
                _expirationDate = SOURCE.getString("expiration_date");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expirationDate;
    }

    public SavedCreditCard expirationDate(String expirationDate) throws ParseException {
        _expirationDate = expirationDate;
        SOURCE.put("expiration_date", expirationDate);
        return this;
    }

    public void setIsExpired(Integer isExpired) throws ParseException {
        _isExpired = isExpired;
        SOURCE.put("isExpired", isExpired);
    }

    public Integer getIsExpired() {
        try {
            if (_isExpired == null && SOURCE.has("isExpired") && SOURCE.get("isExpired") != null)
                _isExpired = SOURCE.getInt("isExpired");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _isExpired;
    }

    public SavedCreditCard isExpired(Integer isExpired) throws ParseException {
        _isExpired = isExpired;
        SOURCE.put("isExpired", isExpired);
        return this;
    }

    public void setLastFour(String lastFour) throws ParseException {
        _lastFour = lastFour;
        SOURCE.put("last_four", lastFour);
    }

    public String getLastFour() {
        try {
            if (_lastFour == null && SOURCE.has("last_four") && SOURCE.get("last_four") != null)
                _lastFour = SOURCE.getString("last_four");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _lastFour;
    }

    public SavedCreditCard lastFour(String lastFour) throws ParseException {
        _lastFour = lastFour;
        SOURCE.put("last_four", lastFour);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum CardTypeEnum {
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

        CardTypeEnum(String value) {
            this.value = value;
        }

        public static CardTypeEnum fromString(String value) {
            CardTypeEnum[] values = values();
            for (CardTypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static CardTypeEnum[] fromJsonArray(JsonArray jsonArray) {
            CardTypeEnum[] list = new CardTypeEnum[jsonArray.size()];
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

    public enum ActionsEnum {
        @Json(name = "deny")
        DENY("deny"),
        @Json(name = "disable")
        DISABLE("disable"),
        @Json(name = "enable")
        ENABLE("enable"),
        @Json(name = "request")
        REQUEST("request");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(SavedCreditCard[] array) {
        JsonArray list = new JsonArray();
        for (SavedCreditCard item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SavedCreditCard[] fromJsonArray(JsonArray array) {
        SavedCreditCard[] list = new SavedCreditCard[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SavedCreditCard fromJson(JsonObject obj) {
        try {
            return new SavedCreditCard(obj);
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
    public static final Parcelable.Creator<SavedCreditCard> CREATOR = new Parcelable.Creator<SavedCreditCard>() {

        @Override
        public SavedCreditCard createFromParcel(Parcel source) {
            try {
                return SavedCreditCard.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SavedCreditCard[] newArray(int size) {
            return new SavedCreditCard[size];
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

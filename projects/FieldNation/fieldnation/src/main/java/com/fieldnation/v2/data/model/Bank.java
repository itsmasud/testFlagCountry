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

public class Bank implements Parcelable {
    private static final String TAG = "Bank";

    @Json(name = "accountCategory")
    private AccountCategoryEnum _accountCategory;

    @Json(name = "accountNumber")
    private String _accountNumber;

    @Json(name = "accountType")
    private AccountTypeEnum _accountType;

    @Json(name = "firstName")
    private String _firstName;

    @Json(name = "lastName")
    private String _lastName;

    @Json(name = "routingNumber")
    private Double _routingNumber;

    @Source
    private JsonObject SOURCE;

    public Bank() {
        SOURCE = new JsonObject();
    }

    public Bank(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAccountCategory(AccountCategoryEnum accountCategory) throws ParseException {
        _accountCategory = accountCategory;
        SOURCE.put("accountCategory", accountCategory.toString());
    }

    public AccountCategoryEnum getAccountCategory() {
        try {
            if (_accountCategory == null && SOURCE.has("accountCategory") && SOURCE.get("accountCategory") != null)
                _accountCategory = AccountCategoryEnum.fromString(SOURCE.getString("accountCategory"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _accountCategory;
    }

    public Bank accountCategory(AccountCategoryEnum accountCategory) throws ParseException {
        _accountCategory = accountCategory;
        SOURCE.put("accountCategory", accountCategory.toString());
        return this;
    }

    public void setAccountNumber(String accountNumber) throws ParseException {
        _accountNumber = accountNumber;
        SOURCE.put("accountNumber", accountNumber);
    }

    public String getAccountNumber() {
        try {
            if (_accountNumber == null && SOURCE.has("accountNumber") && SOURCE.get("accountNumber") != null)
                _accountNumber = SOURCE.getString("accountNumber");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _accountNumber;
    }

    public Bank accountNumber(String accountNumber) throws ParseException {
        _accountNumber = accountNumber;
        SOURCE.put("accountNumber", accountNumber);
        return this;
    }

    public void setAccountType(AccountTypeEnum accountType) throws ParseException {
        _accountType = accountType;
        SOURCE.put("accountType", accountType.toString());
    }

    public AccountTypeEnum getAccountType() {
        try {
            if (_accountType == null && SOURCE.has("accountType") && SOURCE.get("accountType") != null)
                _accountType = AccountTypeEnum.fromString(SOURCE.getString("accountType"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _accountType;
    }

    public Bank accountType(AccountTypeEnum accountType) throws ParseException {
        _accountType = accountType;
        SOURCE.put("accountType", accountType.toString());
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("firstName", firstName);
    }

    public String getFirstName() {
        try {
            if (_firstName == null && SOURCE.has("firstName") && SOURCE.get("firstName") != null)
                _firstName = SOURCE.getString("firstName");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _firstName;
    }

    public Bank firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("firstName", firstName);
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("lastName", lastName);
    }

    public String getLastName() {
        try {
            if (_lastName == null && SOURCE.has("lastName") && SOURCE.get("lastName") != null)
                _lastName = SOURCE.getString("lastName");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _lastName;
    }

    public Bank lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("lastName", lastName);
        return this;
    }

    public void setRoutingNumber(Double routingNumber) throws ParseException {
        _routingNumber = routingNumber;
        SOURCE.put("routingNumber", routingNumber);
    }

    public Double getRoutingNumber() {
        try {
            if (_routingNumber == null && SOURCE.has("routingNumber") && SOURCE.get("routingNumber") != null)
                _routingNumber = SOURCE.getDouble("routingNumber");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _routingNumber;
    }

    public Bank routingNumber(Double routingNumber) throws ParseException {
        _routingNumber = routingNumber;
        SOURCE.put("routingNumber", routingNumber);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum AccountCategoryEnum {
        @Json(name = "Business")
        BUSINESS("Business"),
        @Json(name = "Consumer")
        CONSUMER("Consumer");

        private String value;

        AccountCategoryEnum(String value) {
            this.value = value;
        }

        public static AccountCategoryEnum fromString(String value) {
            AccountCategoryEnum[] values = values();
            for (AccountCategoryEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static AccountCategoryEnum[] fromJsonArray(JsonArray jsonArray) {
            AccountCategoryEnum[] list = new AccountCategoryEnum[jsonArray.size()];
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

    public enum AccountTypeEnum {
        @Json(name = "Checking")
        CHECKING("Checking"),
        @Json(name = "Savings")
        SAVINGS("Savings");

        private String value;

        AccountTypeEnum(String value) {
            this.value = value;
        }

        public static AccountTypeEnum fromString(String value) {
            AccountTypeEnum[] values = values();
            for (AccountTypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static AccountTypeEnum[] fromJsonArray(JsonArray jsonArray) {
            AccountTypeEnum[] list = new AccountTypeEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(Bank[] array) {
        JsonArray list = new JsonArray();
        for (Bank item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Bank[] fromJsonArray(JsonArray array) {
        Bank[] list = new Bank[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Bank fromJson(JsonObject obj) {
        try {
            return new Bank(obj);
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
    public static final Parcelable.Creator<Bank> CREATOR = new Parcelable.Creator<Bank>() {

        @Override
        public Bank createFromParcel(Parcel source) {
            try {
                return Bank.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
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

    public boolean isSet() {
        return true;
    }
}

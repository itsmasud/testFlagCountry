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

public class BankDetails implements Parcelable {
    private static final String TAG = "BankDetails";

    @Json(name = "account_number")
    private String _accountNumber;

    @Json(name = "accountholder_name")
    private String _accountholderName;

    @Json(name = "routing_number")
    private Double _routingNumber;

    @Source
    private JsonObject SOURCE;

    public BankDetails() {
        SOURCE = new JsonObject();
    }

    public BankDetails(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAccountNumber(String accountNumber) throws ParseException {
        _accountNumber = accountNumber;
        SOURCE.put("account_number", accountNumber);
    }

    public String getAccountNumber() {
        try {
            if (_accountNumber == null && SOURCE.has("account_number") && SOURCE.get("account_number") != null)
                _accountNumber = SOURCE.getString("account_number");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _accountNumber;
    }

    public BankDetails accountNumber(String accountNumber) throws ParseException {
        _accountNumber = accountNumber;
        SOURCE.put("account_number", accountNumber);
        return this;
    }

    public void setAccountholderName(String accountholderName) throws ParseException {
        _accountholderName = accountholderName;
        SOURCE.put("accountholder_name", accountholderName);
    }

    public String getAccountholderName() {
        try {
            if (_accountholderName == null && SOURCE.has("accountholder_name") && SOURCE.get("accountholder_name") != null)
                _accountholderName = SOURCE.getString("accountholder_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _accountholderName;
    }

    public BankDetails accountholderName(String accountholderName) throws ParseException {
        _accountholderName = accountholderName;
        SOURCE.put("accountholder_name", accountholderName);
        return this;
    }

    public void setRoutingNumber(Double routingNumber) throws ParseException {
        _routingNumber = routingNumber;
        SOURCE.put("routing_number", routingNumber);
    }

    public Double getRoutingNumber() {
        try {
            if (_routingNumber == null && SOURCE.has("routing_number") && SOURCE.get("routing_number") != null)
                _routingNumber = SOURCE.getDouble("routing_number");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _routingNumber;
    }

    public BankDetails routingNumber(Double routingNumber) throws ParseException {
        _routingNumber = routingNumber;
        SOURCE.put("routing_number", routingNumber);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(BankDetails[] array) {
        JsonArray list = new JsonArray();
        for (BankDetails item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static BankDetails[] fromJsonArray(JsonArray array) {
        BankDetails[] list = new BankDetails[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BankDetails fromJson(JsonObject obj) {
        try {
            return new BankDetails(obj);
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
    public static final Parcelable.Creator<BankDetails> CREATOR = new Parcelable.Creator<BankDetails>() {

        @Override
        public BankDetails createFromParcel(Parcel source) {
            try {
                return BankDetails.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BankDetails[] newArray(int size) {
            return new BankDetails[size];
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

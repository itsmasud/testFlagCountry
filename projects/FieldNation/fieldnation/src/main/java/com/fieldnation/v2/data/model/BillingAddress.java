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

public class BillingAddress implements Parcelable {
    private static final String TAG = "BillingAddress";

    @Json(name = "address")
    private String _address;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "country")
    private String _country;

    @Json(name = "email")
    private String _email;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "state")
    private String _state;

    @Json(name = "zip")
    private String _zip;

    @Source
    private JsonObject SOURCE;

    public BillingAddress() {
        SOURCE = new JsonObject();
    }

    public BillingAddress(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAddress(String address) throws ParseException {
        _address = address;
        SOURCE.put("address", address);
    }

    public String getAddress() {
        try {
            if (_address == null && SOURCE.has("address") && SOURCE.get("address") != null)
                _address = SOURCE.getString("address");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address;
    }

    public BillingAddress address(String address) throws ParseException {
        _address = address;
        SOURCE.put("address", address);
        return this;
    }

    public void setAddress2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
    }

    public String getAddress2() {
        try {
            if (_address2 == null && SOURCE.has("address2") && SOURCE.get("address2") != null)
                _address2 = SOURCE.getString("address2");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address2;
    }

    public BillingAddress address2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        try {
            if (_city == null && SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _city;
    }

    public BillingAddress city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setCountry(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
    }

    public String getCountry() {
        try {
            if (_country == null && SOURCE.has("country") && SOURCE.get("country") != null)
                _country = SOURCE.getString("country");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _country;
    }

    public BillingAddress country(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        try {
            if (_email == null && SOURCE.has("email") && SOURCE.get("email") != null)
                _email = SOURCE.getString("email");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _email;
    }

    public BillingAddress email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        try {
            if (_phone == null && SOURCE.has("phone") && SOURCE.get("phone") != null)
                _phone = SOURCE.getString("phone");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _phone;
    }

    public BillingAddress phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        try {
            if (_state == null && SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _state;
    }

    public BillingAddress state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setZip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public String getZip() {
        try {
            if (_zip == null && SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getString("zip");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _zip;
    }

    public BillingAddress zip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(BillingAddress[] array) {
        JsonArray list = new JsonArray();
        for (BillingAddress item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static BillingAddress[] fromJsonArray(JsonArray array) {
        BillingAddress[] list = new BillingAddress[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BillingAddress fromJson(JsonObject obj) {
        try {
            return new BillingAddress(obj);
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
    public static final Parcelable.Creator<BillingAddress> CREATOR = new Parcelable.Creator<BillingAddress>() {

        @Override
        public BillingAddress createFromParcel(Parcel source) {
            try {
                return BillingAddress.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BillingAddress[] newArray(int size) {
            return new BillingAddress[size];
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

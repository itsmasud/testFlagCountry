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

public class BillingAddress implements Parcelable {
    private static final String TAG = "BillingAddress";

    @Json(name = "address")
    private String _address;

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

    public BillingAddress() {
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getAddress() {
        return _address;
    }

    public BillingAddress address(String address) {
        _address = address;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public BillingAddress city(String city) {
        _city = city;
        return this;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getCountry() {
        return _country;
    }

    public BillingAddress country(String country) {
        _country = country;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public BillingAddress email(String email) {
        _email = email;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public BillingAddress phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public BillingAddress state(String state) {
        _state = state;
        return this;
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getZip() {
        return _zip;
    }

    public BillingAddress zip(String zip) {
        _zip = zip;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static BillingAddress[] fromJsonArray(JsonArray array) {
        BillingAddress[] list = new BillingAddress[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BillingAddress fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(BillingAddress.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BillingAddress billingAddress) {
        try {
            return Serializer.serializeObject(billingAddress);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

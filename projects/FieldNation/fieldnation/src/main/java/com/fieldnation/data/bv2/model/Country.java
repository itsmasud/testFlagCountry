package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class Country implements Parcelable {
    private static final String TAG = "Country";

    @Json(name = "zip")
    private CountryZip _zip;

    @Json(name = "iso")
    private String _iso;

    @Json(name = "address2")
    private CountryAddress2 _address2;

    @Json(name = "city")
    private CountryCity _city;

    @Json(name = "address1")
    private CountryAddress1 _address1;

    @Json(name = "name")
    private String _name;

    @Json(name = "state")
    private CountryState _state;

    public Country() {
    }

    public void setZip(CountryZip zip) {
        _zip = zip;
    }

    public CountryZip getZip() {
        return _zip;
    }

    public Country zip(CountryZip zip) {
        _zip = zip;
        return this;
    }

    public void setIso(String iso) {
        _iso = iso;
    }

    public String getIso() {
        return _iso;
    }

    public Country iso(String iso) {
        _iso = iso;
        return this;
    }

    public void setAddress2(CountryAddress2 address2) {
        _address2 = address2;
    }

    public CountryAddress2 getAddress2() {
        return _address2;
    }

    public Country address2(CountryAddress2 address2) {
        _address2 = address2;
        return this;
    }

    public void setCity(CountryCity city) {
        _city = city;
    }

    public CountryCity getCity() {
        return _city;
    }

    public Country city(CountryCity city) {
        _city = city;
        return this;
    }

    public void setAddress1(CountryAddress1 address1) {
        _address1 = address1;
    }

    public CountryAddress1 getAddress1() {
        return _address1;
    }

    public Country address1(CountryAddress1 address1) {
        _address1 = address1;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Country name(String name) {
        _name = name;
        return this;
    }

    public void setState(CountryState state) {
        _state = state;
    }

    public CountryState getState() {
        return _state;
    }

    public Country state(CountryState state) {
        _state = state;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Country fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Country.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Country country) {
        try {
            return Serializer.serializeObject(country);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {

        @Override
        public Country createFromParcel(Parcel source) {
            try {
                return Country.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
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

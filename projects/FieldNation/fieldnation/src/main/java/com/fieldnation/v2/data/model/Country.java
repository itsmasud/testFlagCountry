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

public class Country implements Parcelable {
    private static final String TAG = "Country";

    @Json(name = "address1")
    private CountryAddress1 _address1;

    @Json(name = "address2")
    private CountryAddress2 _address2;

    @Json(name = "city")
    private CountryCity _city;

    @Json(name = "iso")
    private String _iso;

    @Json(name = "name")
    private String _name;

    @Json(name = "state")
    private CountryState _state;

    @Json(name = "zip")
    private CountryZip _zip;

    @Source
    private JsonObject SOURCE;

    public Country() {
        SOURCE = new JsonObject();
    }

    public Country(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAddress1(CountryAddress1 address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1.getJson());
    }

    public CountryAddress1 getAddress1() {
        try {
            if (_address1 == null && SOURCE.has("address1") && SOURCE.get("address1") != null)
                _address1 = CountryAddress1.fromJson(SOURCE.getJsonObject("address1"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_address1 != null && _address1.isSet())
        return _address1;

        return null;
    }

    public Country address1(CountryAddress1 address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1.getJson());
        return this;
    }

    public void setAddress2(CountryAddress2 address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2.getJson());
    }

    public CountryAddress2 getAddress2() {
        try {
            if (_address2 == null && SOURCE.has("address2") && SOURCE.get("address2") != null)
                _address2 = CountryAddress2.fromJson(SOURCE.getJsonObject("address2"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_address2 != null && _address2.isSet())
        return _address2;

        return null;
    }

    public Country address2(CountryAddress2 address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2.getJson());
        return this;
    }

    public void setCity(CountryCity city) throws ParseException {
        _city = city;
        SOURCE.put("city", city.getJson());
    }

    public CountryCity getCity() {
        try {
            if (_city == null && SOURCE.has("city") && SOURCE.get("city") != null)
                _city = CountryCity.fromJson(SOURCE.getJsonObject("city"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_city != null && _city.isSet())
        return _city;

        return null;
    }

    public Country city(CountryCity city) throws ParseException {
        _city = city;
        SOURCE.put("city", city.getJson());
        return this;
    }

    public void setIso(String iso) throws ParseException {
        _iso = iso;
        SOURCE.put("iso", iso);
    }

    public String getIso() {
        try {
            if (_iso == null && SOURCE.has("iso") && SOURCE.get("iso") != null)
                _iso = SOURCE.getString("iso");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _iso;
    }

    public Country iso(String iso) throws ParseException {
        _iso = iso;
        SOURCE.put("iso", iso);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public Country name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setState(CountryState state) throws ParseException {
        _state = state;
        SOURCE.put("state", state.getJson());
    }

    public CountryState getState() {
        try {
            if (_state == null && SOURCE.has("state") && SOURCE.get("state") != null)
                _state = CountryState.fromJson(SOURCE.getJsonObject("state"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_state != null && _state.isSet())
        return _state;

        return null;
    }

    public Country state(CountryState state) throws ParseException {
        _state = state;
        SOURCE.put("state", state.getJson());
        return this;
    }

    public void setZip(CountryZip zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip.getJson());
    }

    public CountryZip getZip() {
        try {
            if (_zip == null && SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = CountryZip.fromJson(SOURCE.getJsonObject("zip"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_zip != null && _zip.isSet())
        return _zip;

        return null;
    }

    public Country zip(CountryZip zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Country[] array) {
        JsonArray list = new JsonArray();
        for (Country item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Country[] fromJsonArray(JsonArray array) {
        Country[] list = new Country[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Country fromJson(JsonObject obj) {
        try {
            return new Country(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}

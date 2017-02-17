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
    private JsonObject SOURCE = new JsonObject();

    public Country() {
    }

    public void setAddress1(CountryAddress1 address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1.getJson());
    }

    public CountryAddress1 getAddress1() {
        return _address1;
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
        return _address2;
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
        return _city;
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
        return _state;
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
        return _zip;
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
            return Unserializer.unserializeObject(Country.class, obj);
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
}

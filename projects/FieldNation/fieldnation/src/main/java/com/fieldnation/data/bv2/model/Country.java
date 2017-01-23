package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Country {
    private static final String TAG = "Country";

    @Json(name = "name")
    private String name = null;

    @Json(name = "iso")
    private String iso = null;

    @Json(name = "address1")
    private CountryAddress1 address1 = null;

    @Json(name = "address2")
    private CountryAddress1 address2 = null;

    @Json(name = "city")
    private CountryAddress1 city = null;

    @Json(name = "state")
    private CountryState state = null;

    @Json(name = "zip")
    private CountryAddress1 zip = null;

    public Country() {
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }

    public CountryAddress1 getAddress1() {
        return address1;
    }

    public CountryAddress1 getAddress2() {
        return address2;
    }

    public CountryAddress1 getCity() {
        return city;
    }

    public CountryState getState() {
        return state;
    }

    public CountryAddress1 getZip() {
        return zip;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Country fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Country.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}


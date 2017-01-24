package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Country {
    private static final String TAG = "Country";

    @Json(name = "zip")
    private CountryAddress1 zip;

    @Json(name = "iso")
    private String iso;

    @Json(name = "address2")
    private CountryAddress1 address2;

    @Json(name = "city")
    private CountryAddress1 city;

    @Json(name = "address1")
    private CountryAddress1 address1;

    @Json(name = "name")
    private String name;

    @Json(name = "state")
    private CountryState state;

    public Country() {
    }

    public CountryAddress1 getZip() {
        return zip;
    }

    public String getIso() {
        return iso;
    }

    public CountryAddress1 getAddress2() {
        return address2;
    }

    public CountryAddress1 getCity() {
        return city;
    }

    public CountryAddress1 getAddress1() {
        return address1;
    }

    public String getName() {
        return name;
    }

    public CountryState getState() {
        return state;
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

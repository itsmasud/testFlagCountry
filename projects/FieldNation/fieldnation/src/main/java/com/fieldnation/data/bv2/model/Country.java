package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Country {
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

    public CountryZip getZip() {
        return _zip;
    }

    public String getIso() {
        return _iso;
    }

    public CountryAddress2 getAddress2() {
        return _address2;
    }

    public CountryCity getCity() {
        return _city;
    }

    public CountryAddress1 getAddress1() {
        return _address1;
    }

    public String getName() {
        return _name;
    }

    public CountryState getState() {
        return _state;
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
}

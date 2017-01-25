package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Company {
    private static final String TAG = "Company";

    @Json(name = "features")
    private String[] _features;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public Company() {
    }

    public String[] getFeatures() {
        return _features;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Company fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Company.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Company company) {
        try {
            return Serializer.serializeObject(company);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

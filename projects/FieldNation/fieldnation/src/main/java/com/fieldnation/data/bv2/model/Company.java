package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Company {
    private static final String TAG = "Company";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "features")
    private String[] features;

    public Company() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getFeatures() {
        return features;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Company fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Company.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}


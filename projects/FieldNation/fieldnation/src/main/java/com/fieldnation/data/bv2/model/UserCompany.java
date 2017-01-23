package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserCompany {
    private static final String TAG = "UserCompany";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "features")
    private String[] features;

    @Json(name = "technicians")
    private Integer technicians = null;

    @Json(name = "vendors")
    private Company[] vendors;

    public UserCompany() {
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

    public Integer getTechnicians() {
        return technicians;
    }

    public Company[] getVendors() {
        return vendors;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserCompany fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserCompany.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserCompany userCompany) {
        try {
            return Serializer.serializeObject(userCompany);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserCompany {
    private static final String TAG = "UserCompany";

    @Json(name = "features")
    private String[] _features;

    @Json(name = "technicians")
    private Integer _technicians;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "vendors")
    private Company[] _vendors;

    public UserCompany() {
    }

    public String[] getFeatures() {
        return _features;
    }

    public Integer getTechnicians() {
        return _technicians;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public Company[] getVendors() {
        return _vendors;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserCompany fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserCompany.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

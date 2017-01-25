package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserJobs {
    private static final String TAG = "UserJobs";

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Json(name = "company")
    private Integer _company;

    public UserJobs() {
    }

    public Integer getMarketplace() {
        return _marketplace;
    }

    public Integer getCompany() {
        return _company;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserJobs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserJobs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserJobs userJobs) {
        try {
            return Serializer.serializeObject(userJobs);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

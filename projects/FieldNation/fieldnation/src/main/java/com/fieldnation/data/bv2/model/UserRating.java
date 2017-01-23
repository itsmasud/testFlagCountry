package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserRating {
    private static final String TAG = "UserRating";

    @Json(name = "company")
    private Integer company = null;

    @Json(name = "marketplace")
    private Integer marketplace = null;

    public UserRating() {
    }

    public Integer getCompany() {
        return company;
    }

    public Integer getMarketplace() {
        return marketplace;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserRating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserRating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserRating userRating) {
        try {
            return Serializer.serializeObject(userRating);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
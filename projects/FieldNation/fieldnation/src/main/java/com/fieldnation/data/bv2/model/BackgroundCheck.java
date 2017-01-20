package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class BackgroundCheck {
    private static final String TAG = "BackgroundCheck";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "expires")
    private String expires = null;

    public BackgroundCheck() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpires() {
        return expires;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static BackgroundCheck fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(BackgroundCheck.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BackgroundCheck backgroundCheck) {
        try {
            return Serializer.serializeObject(backgroundCheck);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
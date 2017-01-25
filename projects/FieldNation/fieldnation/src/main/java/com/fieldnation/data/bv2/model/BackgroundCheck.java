package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class BackgroundCheck {
    private static final String TAG = "BackgroundCheck";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public BackgroundCheck() {
    }

    public String getExpires() {
        return _expires;
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
    public static BackgroundCheck fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(BackgroundCheck.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationAttribute {
    private static final String TAG = "LocationAttribute";

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "value")
    private String _value;

    @Json(name = "key")
    private String _key;

    public LocationAttribute() {
    }

    public Boolean getPrivate() {
        return _private;
    }

    public String getValue() {
        return _value;
    }

    public String getKey() {
        return _key;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationAttribute fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationAttribute.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationAttribute locationAttribute) {
        try {
            return Serializer.serializeObject(locationAttribute);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

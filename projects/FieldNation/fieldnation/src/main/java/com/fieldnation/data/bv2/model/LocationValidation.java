package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationValidation {
    private static final String TAG = "LocationValidation";

    @Json(name = "is_valid")
    private Boolean _isValid;

    @Json(name = "messages")
    private String[] _messages;

    public LocationValidation() {
    }

    public Boolean getIsValid() {
        return _isValid;
    }

    public String[] getMessages() {
        return _messages;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationValidation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationValidation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationValidation locationValidation) {
        try {
            return Serializer.serializeObject(locationValidation);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

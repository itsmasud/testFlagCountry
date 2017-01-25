package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Error {
    private static final String TAG = "Error";

    @Json(name = "trace")
    private ErrorTrace[] _trace;

    @Json(name = "code")
    private Integer _code;

    @Json(name = "message")
    private String _message;

    @Json(name = "fields")
    private String _fields;

    public Error() {
    }

    public ErrorTrace[] getTrace() {
        return _trace;
    }

    public Integer getCode() {
        return _code;
    }

    public String getMessage() {
        return _message;
    }

    public String getFields() {
        return _fields;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Error fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Error.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Error error) {
        try {
            return Serializer.serializeObject(error);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

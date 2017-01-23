package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ErrorTrace {
    private static final String TAG = "ErrorTrace";

    @Json(name = "file")
    private String file = null;

    @Json(name = "line")
    private Integer line = null;

    @Json(name = "function")
    private String function = null;

    @Json(name = "class")
    private String propertyClass = null;

    @Json(name = "object")
    private String object = null;

    @Json(name = "type")
    private String type = null;

    @Json(name = "args")
    private ErrorTraceArgs[] args;

    public ErrorTrace() {
    }

    public String getFile() {
        return file;
    }

    public Integer getLine() {
        return line;
    }

    public String getFunction() {
        return function;
    }

    public String getPropertyClass() {
        return propertyClass;
    }

    public String getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public ErrorTraceArgs[] getArgs() {
        return args;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTrace fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTrace.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ErrorTrace errorTrace) {
        try {
            return Serializer.serializeObject(errorTrace);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


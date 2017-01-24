package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ErrorTrace {
    private static final String TAG = "ErrorTrace";

    @Json(name = "args")
    private ErrorTraceArgs[] args;

    @Json(name = "file")
    private String file;

    @Json(name = "line")
    private Integer line;

    @Json(name = "function")
    private String function;

    @Json(name = "type")
    private String type;

    @Json(name = "class")
    private String _class;

    // Todo data type not known
    @Json(name = "object")
    private String object;

    public ErrorTrace() {
    }

    public ErrorTraceArgs[] getArgs() {
        return args;
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

    public String getType() {
        return type;
    }

    public String getClassName() {
        return _class;
    }

    public String getObject() {
        return object;
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

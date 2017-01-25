package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ErrorTrace {
    private static final String TAG = "ErrorTrace";

    @Json(name = "args")
    private ErrorTraceArgs[] _args;

    @Json(name = "file")
    private String _file;

    @Json(name = "line")
    private Integer _line;

    @Json(name = "function")
    private String _function;

    @Json(name = "type")
    private String _type;

    @Json(name = "class")
    private String _class;

    @Json(name = "object")
    private ErrorTraceObject _object;

    public ErrorTrace() {
    }

    public ErrorTraceArgs[] getArgs() {
        return _args;
    }

    public String getFile() {
        return _file;
    }

    public Integer getLine() {
        return _line;
    }

    public String getFunction() {
        return _function;
    }

    public String getType() {
        return _type;
    }

    public String getClass() {
        return _class;
    }

    public ErrorTraceObject getObject() {
        return _object;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTrace fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTrace.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

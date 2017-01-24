package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ErrorTraceArgs {
    private static final String TAG = "ErrorTraceArgs";

    @Json(name = "status_code")
    private Integer statusCode;

    @Json(name = "message")
    private String message;

    public ErrorTraceArgs() {
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTraceArgs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTraceArgs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ErrorTraceArgs errorTraceArgs) {
        try {
            return Serializer.serializeObject(errorTraceArgs);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

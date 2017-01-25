package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Problems {
    private static final String TAG = "Problems";

    @Json(name = "next")
    private ProblemsNext[] _next;

    @Json(name = "reason")
    private String _reason;

    @Json(name = "problem_id")
    private Integer _problemId;

    public Problems() {
    }

    public ProblemsNext[] getNext() {
        return _next;
    }

    public String getReason() {
        return _reason;
    }

    public Integer getProblemId() {
        return _problemId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Problems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Problems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Problems problems) {
        try {
            return Serializer.serializeObject(problems);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

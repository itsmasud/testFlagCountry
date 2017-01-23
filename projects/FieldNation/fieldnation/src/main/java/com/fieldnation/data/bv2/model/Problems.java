package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Problems {
    private static final String TAG = "Problems";

    @Json(name = "problem_id")
    private Integer problemId = null;

    @Json(name = "reason")
    private String reason = null;

    @Json(name = "next")
    private ProblemsNext[] next;

    public Problems() {
    }

    public Integer getProblemId() {
        return problemId;
    }

    public String getReason() {
        return reason;
    }

    public ProblemsNext[] getNext() {
        return next;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Problems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Problems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
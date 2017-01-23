package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ProblemsNext {
    private static final String TAG = "ProblemsNext";

    @Json(name = "problem_id")
    private Integer problemId = null;

    @Json(name = "reason")
    private String reason = null;

    public ProblemsNext() {
    }

    public Integer getProblemId() {
        return problemId;
    }

    public String getReason() {
        return reason;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ProblemsNext fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ProblemsNext.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ProblemsNext problemsNext) {
        try {
            return Serializer.serializeObject(problemsNext);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
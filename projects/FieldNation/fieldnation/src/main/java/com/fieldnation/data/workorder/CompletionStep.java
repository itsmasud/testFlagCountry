package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CompletionStep {
    private static final String TAG = "CompletionStep";

    @Json(name = "incompletedFields")
    private String[] _incompletedFields;
    @Json(name = "isCompleted")
    private Boolean _isCompleted;
    @Json(name = "needsVerification")
    private Boolean _needsVerification;
    @Json(name = "text")
    private String _text;
    @Json(name = "type")
    private String _type;

    public CompletionStep() {
    }

    public String[] getIncompletedFields() {
        return _incompletedFields;
    }

    public Boolean getIsCompleted() {
        return _isCompleted;
    }

    public Boolean getNeedsVerification() {
        return _needsVerification;
    }

    public String getText() {
        return _text;
    }

    public String getType() {
        return _type;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompletionStep completionSteps) {
        try {
            return Serializer.serializeObject(completionSteps);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static CompletionStep fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(CompletionStep.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

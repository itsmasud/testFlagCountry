package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageProblem {
    private static final String TAG = "MessageProblem";

    @Json(name = "flag_id")
    private Integer flagId;

    @Json(name = "type")
    private MessageProblemType type;

    @Json(name = "resolved")
    private Boolean resolved;

    public MessageProblem() {
    }

    public Integer getFlagId() {
        return flagId;
    }

    public MessageProblemType getType() {
        return type;
    }

    public Boolean getResolved() {
        return resolved;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageProblem fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageProblem.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MessageProblem messageProblem) {
        try {
            return Serializer.serializeObject(messageProblem);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

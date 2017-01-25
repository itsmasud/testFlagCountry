package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageProblem {
    private static final String TAG = "MessageProblem";

    @Json(name = "flag_id")
    private Integer _flagId;

    @Json(name = "type")
    private MessageProblemType _type;

    @Json(name = "resolved")
    private Boolean _resolved;

    public MessageProblem() {
    }

    public Integer getFlagId() {
        return _flagId;
    }

    public MessageProblemType getType() {
        return _type;
    }

    public Boolean getResolved() {
        return _resolved;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageProblem fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageProblem.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

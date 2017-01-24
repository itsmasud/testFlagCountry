package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class MessageProblemType {
    private static final String TAG = "MessageProblemType";

    @Json(name = "description")
    private String description;

    @Json(name = "id")
    private Integer id;

    public MessageProblemType() {
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageProblemType fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageProblemType.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MessageProblemType messageProblemType) {
        try {
            return Serializer.serializeObject(messageProblemType);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

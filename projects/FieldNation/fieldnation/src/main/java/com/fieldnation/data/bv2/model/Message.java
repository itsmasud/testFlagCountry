package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Message {
    private static final String TAG = "Message";

    @Json(name = "from")
    private MessageFrom from = null;

    @Json(name = "to")
    private MessageTo to = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "msg_id")
    private Integer msgId = null;

    @Json(name = "parent_id")
    private Boolean parentId = null;

    @Json(name = "read")
    private Boolean read = null;

    @Json(name = "created")
    private TimeLogsConfirmed created = null;

    @Json(name = "message")
    private String message = null;

    @Json(name = "problem")
    private MessageProblem problem = null;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "replies")
    private Message replies = null;

    public Message() {
    }

    public MessageFrom getFrom() {
        return from;
    }

    public MessageTo getTo() {
        return to;
    }

    public String getRole() {
        return role;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public Boolean getParentId() {
        return parentId;
    }

    public Boolean getRead() {
        return read;
    }

    public TimeLogsConfirmed getCreated() {
        return created;
    }

    public String getMessage() {
        return message;
    }

    public MessageProblem getProblem() {
        return problem;
    }

    public String[] getActions() {
        return actions;
    }

    public Message getReplies() {
        return replies;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Message fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Message.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Message message) {
        try {
            return Serializer.serializeObject(message);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


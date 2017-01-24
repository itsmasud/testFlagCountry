package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Message {
    private static final String TAG = "Message";

    @Json(name = "role")
    private String role;

    @Json(name = "read")
    private Boolean read;

    @Json(name = "problem")
    private MessageProblem problem;

    @Json(name = "replies")
    private Message replies;

    @Json(name = "parent_id")
    private Boolean parentId;

    @Json(name = "created")
    private Date created;

    @Json(name = "from")
    private MessageFrom from;

    @Json(name = "to")
    private MessageTo to;

    @Json(name = "msg_id")
    private Integer msgId;

    @Json(name = "message")
    private String message;

    @Json(name = "actions")
    private String[] actions;

    public Message() {
    }

    public String getRole() {
        return role;
    }

    public Boolean getRead() {
        return read;
    }

    public MessageProblem getProblem() {
        return problem;
    }

    public Message getReplies() {
        return replies;
    }

    public Boolean getParentId() {
        return parentId;
    }

    public Date getCreated() {
        return created;
    }

    public MessageFrom getFrom() {
        return from;
    }

    public MessageTo getTo() {
        return to;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public String getMessage() {
        return message;
    }

    public String[] getActions() {
        return actions;
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

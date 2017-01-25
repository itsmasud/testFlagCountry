package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Message {
    private static final String TAG = "Message";

    @Json(name = "role")
    private String _role;

    @Json(name = "read")
    private Boolean _read;

    @Json(name = "problem")
    private MessageProblem _problem;

    @Json(name = "replies")
    private Message _replies;

    @Json(name = "parent_id")
    private Boolean _parentId;

    @Json(name = "created")
    private Date _created;

    @Json(name = "from")
    private MessageFrom _from;

    @Json(name = "to")
    private MessageTo _to;

    @Json(name = "msg_id")
    private Integer _msgId;

    @Json(name = "message")
    private String _message;

    @Json(name = "actions")
    private String[] _actions;

    public Message() {
    }

    public String getRole() {
        return _role;
    }

    public Boolean getRead() {
        return _read;
    }

    public MessageProblem getProblem() {
        return _problem;
    }

    public Message getReplies() {
        return _replies;
    }

    public Boolean getParentId() {
        return _parentId;
    }

    public Date getCreated() {
        return _created;
    }

    public MessageFrom getFrom() {
        return _from;
    }

    public MessageTo getTo() {
        return _to;
    }

    public Integer getMsgId() {
        return _msgId;
    }

    public String getMessage() {
        return _message;
    }

    public String[] getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Message fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Message.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

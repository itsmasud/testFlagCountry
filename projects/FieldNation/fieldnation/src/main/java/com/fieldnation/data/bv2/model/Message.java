package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Message implements Parcelable {
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

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Message role(String role) {
        _role = role;
        return this;
    }

    public void setRead(Boolean read) {
        _read = read;
    }

    public Boolean getRead() {
        return _read;
    }

    public Message read(Boolean read) {
        _read = read;
        return this;
    }

    public void setProblem(MessageProblem problem) {
        _problem = problem;
    }

    public MessageProblem getProblem() {
        return _problem;
    }

    public Message problem(MessageProblem problem) {
        _problem = problem;
        return this;
    }

    public void setReplies(Message replies) {
        _replies = replies;
    }

    public Message getReplies() {
        return _replies;
    }

    public Message replies(Message replies) {
        _replies = replies;
        return this;
    }

    public void setParentId(Boolean parentId) {
        _parentId = parentId;
    }

    public Boolean getParentId() {
        return _parentId;
    }

    public Message parentId(Boolean parentId) {
        _parentId = parentId;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Message created(Date created) {
        _created = created;
        return this;
    }

    public void setFrom(MessageFrom from) {
        _from = from;
    }

    public MessageFrom getFrom() {
        return _from;
    }

    public Message from(MessageFrom from) {
        _from = from;
        return this;
    }

    public void setTo(MessageTo to) {
        _to = to;
    }

    public MessageTo getTo() {
        return _to;
    }

    public Message to(MessageTo to) {
        _to = to;
        return this;
    }

    public void setMsgId(Integer msgId) {
        _msgId = msgId;
    }

    public Integer getMsgId() {
        return _msgId;
    }

    public Message msgId(Integer msgId) {
        _msgId = msgId;
        return this;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public Message message(String message) {
        _message = message;
        return this;
    }

    public void setActions(String[] actions) {
        _actions = actions;
    }

    public String[] getActions() {
        return _actions;
    }

    public Message actions(String[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Message[] fromJsonArray(JsonArray array) {
        Message[] list = new Message[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {

        @Override
        public Message createFromParcel(Parcel source) {
            try {
                return Message.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}

package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Message implements Parcelable {
    private static final String TAG = "Message";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "created")
    private Date _created;

    @Json(name = "from")
    private MessageFrom _from;

    @Json(name = "message")
    private String _message;

    @Json(name = "msg_id")
    private Integer _msgId;

    @Json(name = "parent_id")
    private Boolean _parentId;

    @Json(name = "problem")
    private MessageProblem _problem;

    @Json(name = "read")
    private Boolean _read;

    @Json(name = "replies")
    private Message _replies;

    @Json(name = "role")
    private String _role;

    @Json(name = "to")
    private MessageTo _to;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Message() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Message actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public Message created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setFrom(MessageFrom from) throws ParseException {
        _from = from;
        SOURCE.put("from", from.getJson());
    }

    public MessageFrom getFrom() {
        return _from;
    }

    public Message from(MessageFrom from) throws ParseException {
        _from = from;
        SOURCE.put("from", from.getJson());
        return this;
    }

    public void setMessage(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
    }

    public String getMessage() {
        return _message;
    }

    public Message message(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
        return this;
    }

    public void setMsgId(Integer msgId) throws ParseException {
        _msgId = msgId;
        SOURCE.put("msg_id", msgId);
    }

    public Integer getMsgId() {
        return _msgId;
    }

    public Message msgId(Integer msgId) throws ParseException {
        _msgId = msgId;
        SOURCE.put("msg_id", msgId);
        return this;
    }

    public void setParentId(Boolean parentId) throws ParseException {
        _parentId = parentId;
        SOURCE.put("parent_id", parentId);
    }

    public Boolean getParentId() {
        return _parentId;
    }

    public Message parentId(Boolean parentId) throws ParseException {
        _parentId = parentId;
        SOURCE.put("parent_id", parentId);
        return this;
    }

    public void setProblem(MessageProblem problem) throws ParseException {
        _problem = problem;
        SOURCE.put("problem", problem.getJson());
    }

    public MessageProblem getProblem() {
        return _problem;
    }

    public Message problem(MessageProblem problem) throws ParseException {
        _problem = problem;
        SOURCE.put("problem", problem.getJson());
        return this;
    }

    public void setRead(Boolean read) throws ParseException {
        _read = read;
        SOURCE.put("read", read);
    }

    public Boolean getRead() {
        return _read;
    }

    public Message read(Boolean read) throws ParseException {
        _read = read;
        SOURCE.put("read", read);
        return this;
    }

    public void setReplies(Message replies) throws ParseException {
        _replies = replies;
        SOURCE.put("replies", replies.getJson());
    }

    public Message getReplies() {
        return _replies;
    }

    public Message replies(Message replies) throws ParseException {
        _replies = replies;
        SOURCE.put("replies", replies.getJson());
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        return _role;
    }

    public Message role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setTo(MessageTo to) throws ParseException {
        _to = to;
        SOURCE.put("to", to.getJson());
    }

    public MessageTo getTo() {
        return _to;
    }

    public Message to(MessageTo to) throws ParseException {
        _to = to;
        SOURCE.put("to", to.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "unknown")
        UNKNOWN("unknown");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Message[] array) {
        JsonArray list = new JsonArray();
        for (Message item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}

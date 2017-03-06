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

    @Json(name = "sharedText")
    private String _sharedText;

    @Json(name = "to")
    private MessageTo _to;

    @Source
    private JsonObject SOURCE;

    public Message() {
        SOURCE = new JsonObject();
    }

    public Message(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_created != null)
                return _created;

            if (SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_from != null)
                return _from;

            if (SOURCE.has("from") && SOURCE.get("from") != null)
                _from = MessageFrom.fromJson(SOURCE.getJsonObject("from"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_message != null)
                return _message;

            if (SOURCE.has("message") && SOURCE.get("message") != null)
                _message = SOURCE.getString("message");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_msgId != null)
                return _msgId;

            if (SOURCE.has("msg_id") && SOURCE.get("msg_id") != null)
                _msgId = SOURCE.getInt("msg_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_parentId != null)
                return _parentId;

            if (SOURCE.has("parent_id") && SOURCE.get("parent_id") != null)
                _parentId = SOURCE.getBoolean("parent_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_problem != null)
                return _problem;

            if (SOURCE.has("problem") && SOURCE.get("problem") != null)
                _problem = MessageProblem.fromJson(SOURCE.getJsonObject("problem"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_read != null)
                return _read;

            if (SOURCE.has("read") && SOURCE.get("read") != null)
                _read = SOURCE.getBoolean("read");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_replies != null)
                return _replies;

            if (SOURCE.has("replies") && SOURCE.get("replies") != null)
                _replies = Message.fromJson(SOURCE.getJsonObject("replies"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public Message role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setSharedText(String sharedText) throws ParseException {
        _sharedText = sharedText;
        SOURCE.put("sharedText", sharedText);
    }

    public String getSharedText() {
        try {
            if (_sharedText != null)
                return _sharedText;

            if (SOURCE.has("sharedText") && SOURCE.get("sharedText") != null)
                _sharedText = SOURCE.getString("sharedText");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _sharedText;
    }

    public Message sharedText(String sharedText) throws ParseException {
        _sharedText = sharedText;
        SOURCE.put("sharedText", sharedText);
        return this;
    }

    public void setTo(MessageTo to) throws ParseException {
        _to = to;
        SOURCE.put("to", to.getJson());
    }

    public MessageTo getTo() {
        try {
            if (_to != null)
                return _to;

            if (SOURCE.has("to") && SOURCE.get("to") != null)
                _to = MessageTo.fromJson(SOURCE.getJsonObject("to"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        @Json(name = "create")
        CREATE("create"),
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new Message(obj);
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

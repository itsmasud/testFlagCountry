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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Message implements Parcelable {
    private static final String TAG = "Message";

    @Json(name = "actions")
    private String[] _actions;

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

    @Json(name = "read")
    private Boolean _read;

    @Json(name = "replies")
    private Messages _replies;

    @Json(name = "role")
    private String _role;

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

    public void setActions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja);
    }

    public String[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                JsonArray ja = SOURCE.getJsonArray("actions");
                _actions = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _actions;
    }

    public Message actions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
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
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
        return _created;

        return null;
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
            if (_from == null && SOURCE.has("from") && SOURCE.get("from") != null)
                _from = MessageFrom.fromJson(SOURCE.getJsonObject("from"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_from != null && _from.isSet())
        return _from;

        return null;
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
            if (_message == null && SOURCE.has("message") && SOURCE.get("message") != null)
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
            if (_msgId == null && SOURCE.has("msg_id") && SOURCE.get("msg_id") != null)
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
            if (_parentId == null && SOURCE.has("parent_id") && SOURCE.get("parent_id") != null)
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

    public void setRead(Boolean read) throws ParseException {
        _read = read;
        SOURCE.put("read", read);
    }

    public Boolean getRead() {
        try {
            if (_read == null && SOURCE.has("read") && SOURCE.get("read") != null)
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

    public void setReplies(Messages replies) throws ParseException {
        _replies = replies;
        SOURCE.put("replies", replies.getJson());
    }

    public Messages getReplies() {
        try {
            if (_replies == null && SOURCE.has("replies") && SOURCE.get("replies") != null)
                _replies = Messages.fromJson(SOURCE.getJsonObject("replies"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_replies != null && _replies.isSet())
        return _replies;

        return null;
    }

    public Message replies(Messages replies) throws ParseException {
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
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
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

    public void setTo(MessageTo to) throws ParseException {
        _to = to;
        SOURCE.put("to", to.getJson());
    }

    public MessageTo getTo() {
        try {
            if (_to == null && SOURCE.has("to") && SOURCE.get("to") != null)
                _to = MessageTo.fromJson(SOURCE.getJsonObject("to"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_to != null && _to.isSet())
        return _to;

        return null;
    }

    public Message to(MessageTo to) throws ParseException {
        _to = to;
        SOURCE.put("to", to.getJson());
        return this;
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
            Log.v(TAG, ex);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}

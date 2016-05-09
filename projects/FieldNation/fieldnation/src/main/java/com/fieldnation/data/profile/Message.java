package com.fieldnation.data.profile;

import com.fieldnation.Log;
import com.fieldnation.data.workorder.Status;
import com.fieldnation.data.workorder.User;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class Message {
    private static final String TAG = "Message";

    @Json(name = "date")
    private String _date;
    @Json(name = "file_url")
    private String _fileUrl;
    @Json(name = "fromUser")
    private User _fromUser;
    @Json(name = "groupId")
    private Integer _groupId;
    @Json(name = "isRead")
    private Boolean _isRead;
    @Json(name = "message")
    private String _message;
    @Json(name = "messageFrom")
    private String _messageFrom;
    @Json(name = "messageId")
    private Integer _messageId;
    @Json(name = "replies")
    private Replies[] _replies;
    @Json(name = "status")
    private Status _status;
    @Json(name = "workorderId")
    private Long _workorderId;
    @Json(name = "workorderTitle")
    private String _workorderTitle;

    public Message() {
    }

    public String getDate() {
        return _date;
    }

    public String getFileUrl() {
        return _fileUrl;
    }

    public User getFromUser() {
        return _fromUser;
    }

    public Integer getGroupId() {
        return _groupId;
    }

    public Boolean isRead() {
        if (_isRead != null)
            return _isRead;

        return true;
    }

    public String getMessage() {
        return _message;
    }

    public String getMessageFrom() {
        return _messageFrom;
    }

    public Integer getMessageId() {
        return _messageId;
    }

    public Replies[] getReplies() {
        return _replies;
    }

    public Status getStatus() {
        return _status;
    }

    public Long getWorkorderId() {
        return _workorderId;
    }

    public String getWorkorderTitle() {
        return _workorderTitle;
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

    public static Message fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Message.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

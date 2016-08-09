package com.fieldnation.data.profile;

import com.fieldnation.fnlog.Log;
import com.fieldnation.data.workorder.User;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class Notification {
    private static final String TAG = "Notification";
    @Json(name = "date")
    private String _date;
    @Json(name = "fromUser")
    private User _fromUser;
    @Json(name = "message")
    private String _message;
    @Json(name = "notificationId")
    private Integer _notificationId;
    @Json(name = "notificationType")
    private String _notificationType;
    @Json(name = "viewed")
    private Integer _viewed;
    @Json(name = "workorder")
    private Workorder _workorder;
    @Json(name = "workorderId")
    private Long _workorderId;

    public Notification() {
    }

    public String getDate() {
        return _date;
    }

    public User getFromUser() {
        return _fromUser;
    }

    public String getMessage() {
        return _message;
    }

    public Integer getNotificationId() {
        return _notificationId;
    }

    public String getNotificationType() {
        return _notificationType;
    }

    public Integer getViewed() {
        return _viewed;
    }

    public Workorder getWorkorder() {
        return _workorder;
    }

    public Long getWorkorderId() {
        return _workorderId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Notification notification) {
        try {
            return Serializer.serializeObject(notification);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Notification fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Notification.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

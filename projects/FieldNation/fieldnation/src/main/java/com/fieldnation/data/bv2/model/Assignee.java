package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Assignee {
    private static final String TAG = "Assignee";

    @Json(name = "role")
    private String _role;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "user")
    private User _user;

    @Json(name = "actions")
    private String[] _actions;

    public Assignee() {
    }

    public String getRole() {
        return _role;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public User getUser() {
        return _user;
    }

    public String[] getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Assignee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Assignee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Assignee assignee) {
        try {
            return Serializer.serializeObject(assignee);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

import java.util.List;

/**
 * Assignee
 */
public class Assignee {
    private static final String TAG = "Assignee";

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "user")
    private User user = null;

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "status_id")
    private Integer statusId = null;

    @Json(name = "actions")
    private String[] actions = null;

    public Assignee() {
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String[] getActions() {
        return actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Assignee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Assignee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
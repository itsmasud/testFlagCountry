package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Request {
    private static final String TAG = "Request";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "counter")
    private Boolean counter = null;

    @Json(name = "counter_notes")
    private String counterNotes = null;

    @Json(name = "active")
    private Boolean active = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "expires")
    private String expires = null;

    @Json(name = "schedule")
    private Schedule schedule = null;

    @Json(name = "hour_estimate")
    private Double hourEstimate = null;

    @Json(name = "notes")
    private String notes = null;

    @Json(name = "user")
    private User user = null;

    @Json(name = "work_order")
    private WorkOrder workOrder = null;

    @Json(name = "pay")
    private Pay pay = null;

    public Request() {
    }

    public Integer getId() {
        return id;
    }

    public Boolean getCounter() {
        return counter;
    }

    public String getCounterNotes() {
        return counterNotes;
    }

    public Boolean getActive() {
        return active;
    }

    public String getCreated() {
        return created;
    }

    public String getExpires() {
        return expires;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Double getHourEstimate() {
        return hourEstimate;
    }

    public String getNotes() {
        return notes;
    }

    public User getUser() {
        return user;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Pay getPay() {
        return pay;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Request fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Request.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Request request) {
        try {
            return Serializer.serializeObject(request);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
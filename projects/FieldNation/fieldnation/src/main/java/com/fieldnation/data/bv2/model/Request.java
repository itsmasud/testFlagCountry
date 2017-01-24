package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Request {
    private static final String TAG = "Request";

    @Json(name = "schedule")
    private Schedule schedule;

    @Json(name = "expires")
    private Date expires;

    @Json(name = "hour_estimate")
    private Double hourEstimate;

    @Json(name = "notes")
    private String notes;

    @Json(name = "created")
    private Date created;

    @Json(name = "active")
    private Boolean active;

    @Json(name = "pay")
    private Pay pay;

    @Json(name = "counter_notes")
    private String counterNotes;

    @Json(name = "id")
    private Integer id;

    @Json(name = "counter")
    private Boolean counter;

    @Json(name = "user")
    private User user;

    @Json(name = "work_order")
    private WorkOrder workOrder;

    public Request() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Date getExpires() {
        return expires;
    }

    public Double getHourEstimate() {
        return hourEstimate;
    }

    public String getNotes() {
        return notes;
    }

    public Date getCreated() {
        return created;
    }

    public Boolean getActive() {
        return active;
    }

    public Pay getPay() {
        return pay;
    }

    public String getCounterNotes() {
        return counterNotes;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getCounter() {
        return counter;
    }

    public User getUser() {
        return user;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
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

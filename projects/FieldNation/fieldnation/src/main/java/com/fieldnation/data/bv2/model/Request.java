package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Request {
    private static final String TAG = "Request";

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "expires")
    private Date _expires;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "counter_notes")
    private String _counterNotes;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "user")
    private User _user;

    @Json(name = "work_order")
    private WorkOrder _workOrder;

    public Request() {
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public Date getExpires() {
        return _expires;
    }

    public Double getHourEstimate() {
        return _hourEstimate;
    }

    public String getNotes() {
        return _notes;
    }

    public Date getCreated() {
        return _created;
    }

    public Boolean getActive() {
        return _active;
    }

    public Pay getPay() {
        return _pay;
    }

    public String getCounterNotes() {
        return _counterNotes;
    }

    public Integer getId() {
        return _id;
    }

    public Boolean getCounter() {
        return _counter;
    }

    public User getUser() {
        return _user;
    }

    public WorkOrder getWorkOrder() {
        return _workOrder;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Request fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Request.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

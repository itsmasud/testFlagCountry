package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Milestones {
    private static final String TAG = "Milestones";

    @Json(name = "routed")
    private Date routed;

    @Json(name = "canceled")
    private Date canceled;

    @Json(name = "approved")
    private Date approved;

    @Json(name = "time_to_work_done")
    private Double timeToWorkDone;

    @Json(name = "created")
    private Date created;

    @Json(name = "time_to_dispatch")
    private Double timeToDispatch;

    @Json(name = "paid")
    private Date paid;

    @Json(name = "workdone")
    private Date workdone;

    @Json(name = "assigned")
    private Date assigned;

    @Json(name = "published")
    private Date published;

    @Json(name = "time_alive")
    private Double timeAlive;

    public Milestones() {
    }

    public Date getRouted() {
        return routed;
    }

    public Date getCanceled() {
        return canceled;
    }

    public Date getApproved() {
        return approved;
    }

    public Double getTimeToWorkDone() {
        return timeToWorkDone;
    }

    public Date getCreated() {
        return created;
    }

    public Double getTimeToDispatch() {
        return timeToDispatch;
    }

    public Date getPaid() {
        return paid;
    }

    public Date getWorkdone() {
        return workdone;
    }

    public Date getAssigned() {
        return assigned;
    }

    public Date getPublished() {
        return published;
    }

    public Double getTimeAlive() {
        return timeAlive;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Milestones fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Milestones.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Milestones milestones) {
        try {
            return Serializer.serializeObject(milestones);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

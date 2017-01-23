package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Milestones {
    private static final String TAG = "Milestones";

    @Json(name = "time_to_dispatch")
    private Double timeToDispatch = null;

    @Json(name = "time_to_work_done")
    private Double timeToWorkDone = null;

    @Json(name = "time_alive")
    private Double timeAlive = null;

    @Json(name = "created")
    private MilestonesCreated created = null;

    @Json(name = "published")
    private MilestonesCreated published = null;

    @Json(name = "routed")
    private MilestonesCreated routed = null;

    @Json(name = "assigned")
    private MilestonesCreated assigned = null;

    @Json(name = "workdone")
    private MilestonesCreated workdone = null;

    @Json(name = "approved")
    private MilestonesCreated approved = null;

    @Json(name = "paid")
    private MilestonesCreated paid = null;

    @Json(name = "canceled")
    private MilestonesCreated canceled = null;

    public Milestones() {
    }

    public Double getTimeToDispatch() {
        return timeToDispatch;
    }

    public Double getTimeToWorkDone() {
        return timeToWorkDone;
    }

    public Double getTimeAlive() {
        return timeAlive;
    }

    public MilestonesCreated getCreated() {
        return created;
    }

    public MilestonesCreated getPublished() {
        return published;
    }

    public MilestonesCreated getRouted() {
        return routed;
    }

    public MilestonesCreated getAssigned() {
        return assigned;
    }

    public MilestonesCreated getWorkdone() {
        return workdone;
    }

    public MilestonesCreated getApproved() {
        return approved;
    }

    public MilestonesCreated getPaid() {
        return paid;
    }

    public MilestonesCreated getCanceled() {
        return canceled;
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
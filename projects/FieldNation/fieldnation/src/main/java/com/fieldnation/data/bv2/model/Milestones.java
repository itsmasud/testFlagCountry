package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Milestones {
    private static final String TAG = "Milestones";

    @Json(name = "routed")
    private Date _routed;

    @Json(name = "canceled")
    private Date _canceled;

    @Json(name = "approved")
    private Date _approved;

    @Json(name = "time_to_work_done")
    private Double _timeToWorkDone;

    @Json(name = "created")
    private Date _created;

    @Json(name = "time_to_dispatch")
    private Double _timeToDispatch;

    @Json(name = "paid")
    private Date _paid;

    @Json(name = "workdone")
    private Date _workdone;

    @Json(name = "assigned")
    private Date _assigned;

    @Json(name = "published")
    private Date _published;

    @Json(name = "time_alive")
    private Double _timeAlive;

    public Milestones() {
    }

    public Date getRouted() {
        return _routed;
    }

    public Date getCanceled() {
        return _canceled;
    }

    public Date getApproved() {
        return _approved;
    }

    public Double getTimeToWorkDone() {
        return _timeToWorkDone;
    }

    public Date getCreated() {
        return _created;
    }

    public Double getTimeToDispatch() {
        return _timeToDispatch;
    }

    public Date getPaid() {
        return _paid;
    }

    public Date getWorkdone() {
        return _workdone;
    }

    public Date getAssigned() {
        return _assigned;
    }

    public Date getPublished() {
        return _published;
    }

    public Double getTimeAlive() {
        return _timeAlive;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Milestones fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Milestones.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

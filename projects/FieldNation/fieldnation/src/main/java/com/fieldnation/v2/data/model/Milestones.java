package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Milestones implements Parcelable {
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

    public void setRouted(Date routed) {
        _routed = routed;
    }

    public Date getRouted() {
        return _routed;
    }

    public Milestones routed(Date routed) {
        _routed = routed;
        return this;
    }

    public void setCanceled(Date canceled) {
        _canceled = canceled;
    }

    public Date getCanceled() {
        return _canceled;
    }

    public Milestones canceled(Date canceled) {
        _canceled = canceled;
        return this;
    }

    public void setApproved(Date approved) {
        _approved = approved;
    }

    public Date getApproved() {
        return _approved;
    }

    public Milestones approved(Date approved) {
        _approved = approved;
        return this;
    }

    public void setTimeToWorkDone(Double timeToWorkDone) {
        _timeToWorkDone = timeToWorkDone;
    }

    public Double getTimeToWorkDone() {
        return _timeToWorkDone;
    }

    public Milestones timeToWorkDone(Double timeToWorkDone) {
        _timeToWorkDone = timeToWorkDone;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Milestones created(Date created) {
        _created = created;
        return this;
    }

    public void setTimeToDispatch(Double timeToDispatch) {
        _timeToDispatch = timeToDispatch;
    }

    public Double getTimeToDispatch() {
        return _timeToDispatch;
    }

    public Milestones timeToDispatch(Double timeToDispatch) {
        _timeToDispatch = timeToDispatch;
        return this;
    }

    public void setPaid(Date paid) {
        _paid = paid;
    }

    public Date getPaid() {
        return _paid;
    }

    public Milestones paid(Date paid) {
        _paid = paid;
        return this;
    }

    public void setWorkdone(Date workdone) {
        _workdone = workdone;
    }

    public Date getWorkdone() {
        return _workdone;
    }

    public Milestones workdone(Date workdone) {
        _workdone = workdone;
        return this;
    }

    public void setAssigned(Date assigned) {
        _assigned = assigned;
    }

    public Date getAssigned() {
        return _assigned;
    }

    public Milestones assigned(Date assigned) {
        _assigned = assigned;
        return this;
    }

    public void setPublished(Date published) {
        _published = published;
    }

    public Date getPublished() {
        return _published;
    }

    public Milestones published(Date published) {
        _published = published;
        return this;
    }

    public void setTimeAlive(Double timeAlive) {
        _timeAlive = timeAlive;
    }

    public Double getTimeAlive() {
        return _timeAlive;
    }

    public Milestones timeAlive(Double timeAlive) {
        _timeAlive = timeAlive;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Milestones[] fromJsonArray(JsonArray array) {
        Milestones[] list = new Milestones[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Milestones> CREATOR = new Parcelable.Creator<Milestones>() {

        @Override
        public Milestones createFromParcel(Parcel source) {
            try {
                return Milestones.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Milestones[] newArray(int size) {
            return new Milestones[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}

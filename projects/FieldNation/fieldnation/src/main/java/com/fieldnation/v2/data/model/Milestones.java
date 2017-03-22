package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Milestones implements Parcelable {
    private static final String TAG = "Milestones";

    @Json(name = "approved")
    private Date _approved;

    @Json(name = "assigned")
    private Date _assigned;

    @Json(name = "canceled")
    private Date _canceled;

    @Json(name = "created")
    private Date _created;

    @Json(name = "paid")
    private Date _paid;

    @Json(name = "published")
    private Date _published;

    @Json(name = "routed")
    private Date _routed;

    @Json(name = "time_alive")
    private Double _timeAlive;

    @Json(name = "time_to_dispatch")
    private Double _timeToDispatch;

    @Json(name = "time_to_work_done")
    private Double _timeToWorkDone;

    @Json(name = "workdone")
    private Date _workdone;

    @Source
    private JsonObject SOURCE;

    public Milestones() {
        SOURCE = new JsonObject();
    }

    public Milestones(JsonObject obj) {
        SOURCE = obj;
    }

    public void setApproved(Date approved) throws ParseException {
        _approved = approved;
        SOURCE.put("approved", approved.getJson());
    }

    public Date getApproved() {
        try {
            if (_approved == null && SOURCE.has("approved") && SOURCE.get("approved") != null)
                _approved = Date.fromJson(SOURCE.getJsonObject("approved"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_approved != null && _approved.isSet())
        return _approved;

        return null;
    }

    public Milestones approved(Date approved) throws ParseException {
        _approved = approved;
        SOURCE.put("approved", approved.getJson());
        return this;
    }

    public void setAssigned(Date assigned) throws ParseException {
        _assigned = assigned;
        SOURCE.put("assigned", assigned.getJson());
    }

    public Date getAssigned() {
        try {
            if (_assigned == null && SOURCE.has("assigned") && SOURCE.get("assigned") != null)
                _assigned = Date.fromJson(SOURCE.getJsonObject("assigned"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_assigned != null && _assigned.isSet())
        return _assigned;

        return null;
    }

    public Milestones assigned(Date assigned) throws ParseException {
        _assigned = assigned;
        SOURCE.put("assigned", assigned.getJson());
        return this;
    }

    public void setCanceled(Date canceled) throws ParseException {
        _canceled = canceled;
        SOURCE.put("canceled", canceled.getJson());
    }

    public Date getCanceled() {
        try {
            if (_canceled == null && SOURCE.has("canceled") && SOURCE.get("canceled") != null)
                _canceled = Date.fromJson(SOURCE.getJsonObject("canceled"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_canceled != null && _canceled.isSet())
        return _canceled;

        return null;
    }

    public Milestones canceled(Date canceled) throws ParseException {
        _canceled = canceled;
        SOURCE.put("canceled", canceled.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
        return _created;

        return null;
    }

    public Milestones created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setPaid(Date paid) throws ParseException {
        _paid = paid;
        SOURCE.put("paid", paid.getJson());
    }

    public Date getPaid() {
        try {
            if (_paid == null && SOURCE.has("paid") && SOURCE.get("paid") != null)
                _paid = Date.fromJson(SOURCE.getJsonObject("paid"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_paid != null && _paid.isSet())
        return _paid;

        return null;
    }

    public Milestones paid(Date paid) throws ParseException {
        _paid = paid;
        SOURCE.put("paid", paid.getJson());
        return this;
    }

    public void setPublished(Date published) throws ParseException {
        _published = published;
        SOURCE.put("published", published.getJson());
    }

    public Date getPublished() {
        try {
            if (_published == null && SOURCE.has("published") && SOURCE.get("published") != null)
                _published = Date.fromJson(SOURCE.getJsonObject("published"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_published != null && _published.isSet())
        return _published;

        return null;
    }

    public Milestones published(Date published) throws ParseException {
        _published = published;
        SOURCE.put("published", published.getJson());
        return this;
    }

    public void setRouted(Date routed) throws ParseException {
        _routed = routed;
        SOURCE.put("routed", routed.getJson());
    }

    public Date getRouted() {
        try {
            if (_routed == null && SOURCE.has("routed") && SOURCE.get("routed") != null)
                _routed = Date.fromJson(SOURCE.getJsonObject("routed"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_routed != null && _routed.isSet())
        return _routed;

        return null;
    }

    public Milestones routed(Date routed) throws ParseException {
        _routed = routed;
        SOURCE.put("routed", routed.getJson());
        return this;
    }

    public void setTimeAlive(Double timeAlive) throws ParseException {
        _timeAlive = timeAlive;
        SOURCE.put("time_alive", timeAlive);
    }

    public Double getTimeAlive() {
        try {
            if (_timeAlive == null && SOURCE.has("time_alive") && SOURCE.get("time_alive") != null)
                _timeAlive = SOURCE.getDouble("time_alive");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeAlive;
    }

    public Milestones timeAlive(Double timeAlive) throws ParseException {
        _timeAlive = timeAlive;
        SOURCE.put("time_alive", timeAlive);
        return this;
    }

    public void setTimeToDispatch(Double timeToDispatch) throws ParseException {
        _timeToDispatch = timeToDispatch;
        SOURCE.put("time_to_dispatch", timeToDispatch);
    }

    public Double getTimeToDispatch() {
        try {
            if (_timeToDispatch == null && SOURCE.has("time_to_dispatch") && SOURCE.get("time_to_dispatch") != null)
                _timeToDispatch = SOURCE.getDouble("time_to_dispatch");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeToDispatch;
    }

    public Milestones timeToDispatch(Double timeToDispatch) throws ParseException {
        _timeToDispatch = timeToDispatch;
        SOURCE.put("time_to_dispatch", timeToDispatch);
        return this;
    }

    public void setTimeToWorkDone(Double timeToWorkDone) throws ParseException {
        _timeToWorkDone = timeToWorkDone;
        SOURCE.put("time_to_work_done", timeToWorkDone);
    }

    public Double getTimeToWorkDone() {
        try {
            if (_timeToWorkDone == null && SOURCE.has("time_to_work_done") && SOURCE.get("time_to_work_done") != null)
                _timeToWorkDone = SOURCE.getDouble("time_to_work_done");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeToWorkDone;
    }

    public Milestones timeToWorkDone(Double timeToWorkDone) throws ParseException {
        _timeToWorkDone = timeToWorkDone;
        SOURCE.put("time_to_work_done", timeToWorkDone);
        return this;
    }

    public void setWorkdone(Date workdone) throws ParseException {
        _workdone = workdone;
        SOURCE.put("workdone", workdone.getJson());
    }

    public Date getWorkdone() {
        try {
            if (_workdone == null && SOURCE.has("workdone") && SOURCE.get("workdone") != null)
                _workdone = Date.fromJson(SOURCE.getJsonObject("workdone"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workdone != null && _workdone.isSet())
        return _workdone;

        return null;
    }

    public Milestones workdone(Date workdone) throws ParseException {
        _workdone = workdone;
        SOURCE.put("workdone", workdone.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Milestones[] array) {
        JsonArray list = new JsonArray();
        for (Milestones item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Milestones[] fromJsonArray(JsonArray array) {
        Milestones[] list = new Milestones[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Milestones fromJson(JsonObject obj) {
        try {
            return new Milestones(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}

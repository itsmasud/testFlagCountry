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
 * Created by dmgen from swagger.
 */

public class Request implements Parcelable {
    private static final String TAG = "Request";

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "counter_notes")
    private String _counterNotes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "expires")
    private Date _expires;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "user")
    private User _user;

    @Json(name = "work_order")
    private WorkOrder _workOrder;

    public Request() {
    }

    public void setActive(Boolean active) {
        _active = active;
    }

    public Boolean getActive() {
        return _active;
    }

    public Request active(Boolean active) {
        _active = active;
        return this;
    }

    public void setCounter(Boolean counter) {
        _counter = counter;
    }

    public Boolean getCounter() {
        return _counter;
    }

    public Request counter(Boolean counter) {
        _counter = counter;
        return this;
    }

    public void setCounterNotes(String counterNotes) {
        _counterNotes = counterNotes;
    }

    public String getCounterNotes() {
        return _counterNotes;
    }

    public Request counterNotes(String counterNotes) {
        _counterNotes = counterNotes;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Request created(Date created) {
        _created = created;
        return this;
    }

    public void setExpires(Date expires) {
        _expires = expires;
    }

    public Date getExpires() {
        return _expires;
    }

    public Request expires(Date expires) {
        _expires = expires;
        return this;
    }

    public void setHourEstimate(Double hourEstimate) {
        _hourEstimate = hourEstimate;
    }

    public Double getHourEstimate() {
        return _hourEstimate;
    }

    public Request hourEstimate(Double hourEstimate) {
        _hourEstimate = hourEstimate;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Request id(Integer id) {
        _id = id;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public Request notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setPay(Pay pay) {
        _pay = pay;
    }

    public Pay getPay() {
        return _pay;
    }

    public Request pay(Pay pay) {
        _pay = pay;
        return this;
    }

    public void setSchedule(Schedule schedule) {
        _schedule = schedule;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public Request schedule(Schedule schedule) {
        _schedule = schedule;
        return this;
    }

    public void setUser(User user) {
        _user = user;
    }

    public User getUser() {
        return _user;
    }

    public Request user(User user) {
        _user = user;
        return this;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
    }

    public WorkOrder getWorkOrder() {
        return _workOrder;
    }

    public Request workOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Request[] fromJsonArray(JsonArray array) {
        Request[] list = new Request[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {

        @Override
        public Request createFromParcel(Parcel source) {
            try {
                return Request.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
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

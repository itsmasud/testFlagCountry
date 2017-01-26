package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class Assignee implements Parcelable {
    private static final String TAG = "Assignee";

    @Json(name = "role")
    private String _role;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "user")
    private User _user;

    @Json(name = "actions")
    private String[] _actions;

    public Assignee() {
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Assignee role(String role) {
        _role = role;
        return this;
    }

    public void setStatusId(Integer statusId) {
        _statusId = statusId;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Assignee statusId(Integer statusId) {
        _statusId = statusId;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public Assignee workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Assignee correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setUser(User user) {
        _user = user;
    }

    public User getUser() {
        return _user;
    }

    public Assignee user(User user) {
        _user = user;
        return this;
    }

    public void setActions(String[] actions) {
        _actions = actions;
    }

    public String[] getActions() {
        return _actions;
    }

    public Assignee actions(String[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Assignee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Assignee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Assignee> CREATOR = new Parcelable.Creator<Assignee>() {

        @Override
        public Assignee createFromParcel(Parcel source) {
            try {
                return Assignee.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Assignee[] newArray(int size) {
            return new Assignee[size];
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

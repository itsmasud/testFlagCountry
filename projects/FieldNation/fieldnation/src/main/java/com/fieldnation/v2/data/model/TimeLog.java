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

public class TimeLog implements Parcelable {
    private static final String TAG = "TimeLog";

    @Json(name = "logged_by")
    private User _loggedBy;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "in")
    private CheckInOut _in;

    @Json(name = "devices")
    private Double _devices;

    @Json(name = "verified")
    private Boolean _verified;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "was_late")
    private Boolean _wasLate;

    @Json(name = "out")
    private CheckInOut _out;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    public TimeLog() {
    }

    public void setLoggedBy(User loggedBy) {
        _loggedBy = loggedBy;
    }

    public User getLoggedBy() {
        return _loggedBy;
    }

    public TimeLog loggedBy(User loggedBy) {
        _loggedBy = loggedBy;
        return this;
    }

    public void setHours(Double hours) {
        _hours = hours;
    }

    public Double getHours() {
        return _hours;
    }

    public TimeLog hours(Double hours) {
        _hours = hours;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public TimeLog workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setIn(CheckInOut in) {
        _in = in;
    }

    public CheckInOut getIn() {
        return _in;
    }

    public TimeLog in(CheckInOut in) {
        _in = in;
        return this;
    }

    public void setDevices(Double devices) {
        _devices = devices;
    }

    public Double getDevices() {
        return _devices;
    }

    public TimeLog devices(Double devices) {
        _devices = devices;
        return this;
    }

    public void setVerified(Boolean verified) {
        _verified = verified;
    }

    public Boolean getVerified() {
        return _verified;
    }

    public TimeLog verified(Boolean verified) {
        _verified = verified;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public TimeLog timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public TimeLog actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setWasLate(Boolean wasLate) {
        _wasLate = wasLate;
    }

    public Boolean getWasLate() {
        return _wasLate;
    }

    public TimeLog wasLate(Boolean wasLate) {
        _wasLate = wasLate;
        return this;
    }

    public void setOut(CheckInOut out) {
        _out = out;
    }

    public CheckInOut getOut() {
        return _out;
    }

    public TimeLog out(CheckInOut out) {
        _out = out;
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    public TimeLog onMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TimeLog[] fromJsonArray(JsonArray array) {
        TimeLog[] list = new TimeLog[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TimeLog fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeLog.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeLog timeLog) {
        try {
            return Serializer.serializeObject(timeLog);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<TimeLog> CREATOR = new Parcelable.Creator<TimeLog>() {

        @Override
        public TimeLog createFromParcel(Parcel source) {
            try {
                return TimeLog.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TimeLog[] newArray(int size) {
            return new TimeLog[size];
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

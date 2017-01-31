package com.fieldnation.data.bv2.model;
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

public class Schedule implements Parcelable {
    private static final String TAG = "Schedule";

    @Json(name = "no_refresh")
    private Boolean _noRefresh;

    @Json(name = "eta")
    private ScheduleEta _eta;

    @Json(name = "role")
    private String _role;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "service_window")
    private ScheduleServiceWindow _serviceWindow;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    public Schedule() {
    }

    public void setNoRefresh(Boolean noRefresh) {
        _noRefresh = noRefresh;
    }

    public Boolean getNoRefresh() {
        return _noRefresh;
    }

    public Schedule noRefresh(Boolean noRefresh) {
        _noRefresh = noRefresh;
        return this;
    }

    public void setEta(ScheduleEta eta) {
        _eta = eta;
    }

    public ScheduleEta getEta() {
        return _eta;
    }

    public Schedule eta(ScheduleEta eta) {
        _eta = eta;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Schedule role(String role) {
        _role = role;
        return this;
    }

    public void setStatusId(Integer statusId) {
        _statusId = statusId;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Schedule statusId(Integer statusId) {
        _statusId = statusId;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public Schedule workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setServiceWindow(ScheduleServiceWindow serviceWindow) {
        _serviceWindow = serviceWindow;
    }

    public ScheduleServiceWindow getServiceWindow() {
        return _serviceWindow;
    }

    public Schedule serviceWindow(ScheduleServiceWindow serviceWindow) {
        _serviceWindow = serviceWindow;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Schedule correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Schedule timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Schedule actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    public Schedule onMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Schedule[] fromJsonArray(JsonArray array) {
        Schedule[] list = new Schedule[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Schedule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Schedule.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Schedule schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

        @Override
        public Schedule createFromParcel(Parcel source) {
            try {
                return Schedule.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
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

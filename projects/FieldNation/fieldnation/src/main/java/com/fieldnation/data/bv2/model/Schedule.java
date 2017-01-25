package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Schedule {
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

    public Boolean getNoRefresh() {
        return _noRefresh;
    }

    public ScheduleEta getEta() {
        return _eta;
    }

    public String getRole() {
        return _role;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public ScheduleServiceWindow getServiceWindow() {
        return _serviceWindow;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}

package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Schedule {
    private static final String TAG = "Schedule";

    @Json(name = "no_refresh")
    private Boolean noRefresh;

    @Json(name = "eta")
    private ScheduleEta eta;

    @Json(name = "role")
    private String role;

    @Json(name = "status_id")
    private Integer statusId;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    @Json(name = "service_window")
    private ScheduleServiceWindow serviceWindow;

    @Json(name = "correlation_id")
    private String correlationId;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay;

    public Schedule() {
    }

    public Boolean getNoRefresh() {
        return noRefresh;
    }

    public ScheduleEta getEta() {
        return eta;
    }

    public String getRole() {
        return role;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public ScheduleServiceWindow getServiceWindow() {
        return serviceWindow;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Schedule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Schedule.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

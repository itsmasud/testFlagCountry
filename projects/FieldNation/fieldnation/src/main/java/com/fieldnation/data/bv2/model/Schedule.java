package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Schedule {
    private static final String TAG = "Schedule";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "service_window")
    private ScheduleServiceWindow serviceWindow = null;

    @Json(name = "eta")
    private ScheduleEta eta = null;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "status_id")
    private Integer statusId = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "no_refresh")
    private Boolean noRefresh = null;

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public Schedule() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public ScheduleServiceWindow getServiceWindow() {
        return serviceWindow;
    }

    public ScheduleEta getEta() {
        return eta;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public String getRole() {
        return role;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public Boolean getNoRefresh() {
        return noRefresh;
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
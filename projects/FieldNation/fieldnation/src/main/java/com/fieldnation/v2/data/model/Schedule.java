package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Schedule implements Parcelable {
    private static final String TAG = "Schedule";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "eta")
    private ScheduleEta _eta;

    @Json(name = "no_refresh")
    private Boolean _noRefresh;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    @Json(name = "role")
    private String _role;

    @Json(name = "service_window")
    private ScheduleServiceWindow _serviceWindow;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE;

    public Schedule() {
        SOURCE = new JsonObject();
    }

    public Schedule(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _actions;
    }

    public Schedule actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId != null)
                return _correlationId;

            if (SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public Schedule correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setEta(ScheduleEta eta) throws ParseException {
        _eta = eta;
        SOURCE.put("eta", eta.getJson());
    }

    public ScheduleEta getEta() {
        try {
            if (_eta != null)
                return _eta;

            if (SOURCE.has("eta") && SOURCE.get("eta") != null)
                _eta = ScheduleEta.fromJson(SOURCE.getJsonObject("eta"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _eta;
    }

    public Schedule eta(ScheduleEta eta) throws ParseException {
        _eta = eta;
        SOURCE.put("eta", eta.getJson());
        return this;
    }

    public void setNoRefresh(Boolean noRefresh) throws ParseException {
        _noRefresh = noRefresh;
        SOURCE.put("no_refresh", noRefresh);
    }

    public Boolean getNoRefresh() {
        try {
            if (_noRefresh != null)
                return _noRefresh;

            if (SOURCE.has("no_refresh") && SOURCE.get("no_refresh") != null)
                _noRefresh = SOURCE.getBoolean("no_refresh");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _noRefresh;
    }

    public Schedule noRefresh(Boolean noRefresh) throws ParseException {
        _noRefresh = noRefresh;
        SOURCE.put("no_refresh", noRefresh);
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
    }

    public OnMyWay getOnMyWay() {
        try {
            if (_onMyWay != null)
                return _onMyWay;

            if (SOURCE.has("on_my_way") && SOURCE.get("on_my_way") != null)
                _onMyWay = OnMyWay.fromJson(SOURCE.getJsonObject("on_my_way"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _onMyWay;
    }

    public Schedule onMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public Schedule role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setServiceWindow(ScheduleServiceWindow serviceWindow) throws ParseException {
        _serviceWindow = serviceWindow;
        SOURCE.put("service_window", serviceWindow.getJson());
    }

    public ScheduleServiceWindow getServiceWindow() {
        try {
            if (_serviceWindow != null)
                return _serviceWindow;

            if (SOURCE.has("service_window") && SOURCE.get("service_window") != null)
                _serviceWindow = ScheduleServiceWindow.fromJson(SOURCE.getJsonObject("service_window"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _serviceWindow;
    }

    public Schedule serviceWindow(ScheduleServiceWindow serviceWindow) throws ParseException {
        _serviceWindow = serviceWindow;
        SOURCE.put("service_window", serviceWindow.getJson());
        return this;
    }

    public void setStatusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
    }

    public Integer getStatusId() {
        try {
            if (_statusId != null)
                return _statusId;

            if (SOURCE.has("status_id") && SOURCE.get("status_id") != null)
                _statusId = SOURCE.getInt("status_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusId;
    }

    public Schedule statusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        try {
            if (_timeZone != null)
                return _timeZone;

            if (SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeZone;
    }

    public Schedule timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId != null)
                return _workOrderId;

            if (SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public Schedule workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "eta")
        ETA("eta");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Schedule[] array) {
        JsonArray list = new JsonArray();
        for (Schedule item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Schedule[] fromJsonArray(JsonArray array) {
        Schedule[] list = new Schedule[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Schedule fromJson(JsonObject obj) {
        try {
            return new Schedule(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }

    public boolean isSet(){
        return getServiceWindow() != null && getServiceWindow().isSet();
    }
}

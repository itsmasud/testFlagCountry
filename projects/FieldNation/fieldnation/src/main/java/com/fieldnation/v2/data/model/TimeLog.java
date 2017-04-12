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

public class TimeLog implements Parcelable {
    private static final String TAG = "TimeLog";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "devices")
    private Double _devices;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "in")
    private CheckInOut _in;

    @Json(name = "logged_by")
    private User _loggedBy;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    @Json(name = "out")
    private CheckInOut _out;

    @Json(name = "should_verify")
    private Boolean _shouldVerify;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "verified")
    private Boolean _verified;

    @Json(name = "was_late")
    private Boolean _wasLate;

    @Source
    private JsonObject SOURCE;

    public TimeLog() {
        SOURCE = new JsonObject();
    }

    public TimeLog(JsonObject obj) {
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

    public TimeLog actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setDevices(Double devices) throws ParseException {
        _devices = devices;
        SOURCE.put("devices", devices);
    }

    public Double getDevices() {
        try {
            if (_devices == null && SOURCE.has("devices") && SOURCE.get("devices") != null)
                _devices = SOURCE.getDouble("devices");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _devices;
    }

    public TimeLog devices(Double devices) throws ParseException {
        _devices = devices;
        SOURCE.put("devices", devices);
        return this;
    }

    public void setHours(Double hours) throws ParseException {
        _hours = hours;
        SOURCE.put("hours", hours);
    }

    public Double getHours() {
        try {
            if (_hours == null && SOURCE.has("hours") && SOURCE.get("hours") != null)
                _hours = SOURCE.getDouble("hours");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hours;
    }

    public TimeLog hours(Double hours) throws ParseException {
        _hours = hours;
        SOURCE.put("hours", hours);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public TimeLog id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setIn(CheckInOut in) throws ParseException {
        _in = in;
        SOURCE.put("in", in.getJson());
    }

    public CheckInOut getIn() {
        try {
            if (_in == null && SOURCE.has("in") && SOURCE.get("in") != null)
                _in = CheckInOut.fromJson(SOURCE.getJsonObject("in"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_in != null && _in.isSet())
        return _in;

        return null;
    }

    public TimeLog in(CheckInOut in) throws ParseException {
        _in = in;
        SOURCE.put("in", in.getJson());
        return this;
    }

    public void setLoggedBy(User loggedBy) throws ParseException {
        _loggedBy = loggedBy;
        SOURCE.put("logged_by", loggedBy.getJson());
    }

    public User getLoggedBy() {
        try {
            if (_loggedBy == null && SOURCE.has("logged_by") && SOURCE.get("logged_by") != null)
                _loggedBy = User.fromJson(SOURCE.getJsonObject("logged_by"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_loggedBy != null && _loggedBy.isSet())
        return _loggedBy;

        return null;
    }

    public TimeLog loggedBy(User loggedBy) throws ParseException {
        _loggedBy = loggedBy;
        SOURCE.put("logged_by", loggedBy.getJson());
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
    }

    public OnMyWay getOnMyWay() {
        try {
            if (_onMyWay == null && SOURCE.has("on_my_way") && SOURCE.get("on_my_way") != null)
                _onMyWay = OnMyWay.fromJson(SOURCE.getJsonObject("on_my_way"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_onMyWay != null && _onMyWay.isSet())
        return _onMyWay;

        return null;
    }

    public TimeLog onMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
        return this;
    }

    public void setOut(CheckInOut out) throws ParseException {
        _out = out;
        SOURCE.put("out", out.getJson());
    }

    public CheckInOut getOut() {
        try {
            if (_out == null && SOURCE.has("out") && SOURCE.get("out") != null)
                _out = CheckInOut.fromJson(SOURCE.getJsonObject("out"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_out != null && _out.isSet())
        return _out;

        return null;
    }

    public TimeLog out(CheckInOut out) throws ParseException {
        _out = out;
        SOURCE.put("out", out.getJson());
        return this;
    }

    public void setShouldVerify(Boolean shouldVerify) throws ParseException {
        _shouldVerify = shouldVerify;
        SOURCE.put("should_verify", shouldVerify);
    }

    public Boolean getShouldVerify() {
        try {
            if (_shouldVerify == null && SOURCE.has("should_verify") && SOURCE.get("should_verify") != null)
                _shouldVerify = SOURCE.getBoolean("should_verify");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _shouldVerify;
    }

    public TimeLog shouldVerify(Boolean shouldVerify) throws ParseException {
        _shouldVerify = shouldVerify;
        SOURCE.put("should_verify", shouldVerify);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public TimeLog status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        try {
            if (_timeZone == null && SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_timeZone != null && _timeZone.isSet())
        return _timeZone;

        return null;
    }

    public TimeLog timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setVerified(Boolean verified) throws ParseException {
        _verified = verified;
        SOURCE.put("verified", verified);
    }

    public Boolean getVerified() {
        try {
            if (_verified == null && SOURCE.has("verified") && SOURCE.get("verified") != null)
                _verified = SOURCE.getBoolean("verified");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _verified;
    }

    public TimeLog verified(Boolean verified) throws ParseException {
        _verified = verified;
        SOURCE.put("verified", verified);
        return this;
    }

    public void setWasLate(Boolean wasLate) throws ParseException {
        _wasLate = wasLate;
        SOURCE.put("was_late", wasLate);
    }

    public Boolean getWasLate() {
        try {
            if (_wasLate == null && SOURCE.has("was_late") && SOURCE.get("was_late") != null)
                _wasLate = SOURCE.getBoolean("was_late");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _wasLate;
    }

    public TimeLog wasLate(Boolean wasLate) throws ParseException {
        _wasLate = wasLate;
        SOURCE.put("was_late", wasLate);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "checked_in")
        CHECKED_IN("checked_in"),
        @Json(name = "checked_out")
        CHECKED_OUT("checked_out");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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

    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "verify")
        VERIFY("verify");

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
    public static JsonArray toJsonArray(TimeLog[] array) {
        JsonArray list = new JsonArray();
        for (TimeLog item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TimeLog[] fromJsonArray(JsonArray array) {
        TimeLog[] list = new TimeLog[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TimeLog fromJson(JsonObject obj) {
        try {
            return new TimeLog(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }

    private Set<TimeLog.ActionsEnum> _actionsSet = null;

    public Set<TimeLog.ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }
}

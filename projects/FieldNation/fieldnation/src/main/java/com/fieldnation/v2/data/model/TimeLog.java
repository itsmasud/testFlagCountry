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

import java.text.ParseException;

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

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public TimeLog() {
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
        return _in;
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
        return _loggedBy;
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
        return _onMyWay;
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
        return _out;
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
        return _timeZone;
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
        return _wasLate;
    }

    public TimeLog wasLate(Boolean wasLate) throws ParseException {
        _wasLate = wasLate;
        SOURCE.put("was_late", wasLate);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public TimeLog workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
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

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "verify")
        VERIFY("verify");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
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
            return Unserializer.unserializeObject(TimeLog.class, obj);
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
}

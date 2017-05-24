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

public class TimeLogs implements Parcelable {
    private static final String TAG = "TimeLogs";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "confirmed")
    private Date _confirmed;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "onmyway")
    private OnMyWay _onmyway;

    @Json(name = "open_time_log")
    private TimeLog _openTimeLog;

    @Json(name = "results")
    private TimeLog[] _results;

    @Json(name = "status")
    private String _status;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Source
    private JsonObject SOURCE;

    public TimeLogs() {
        SOURCE = new JsonObject();
    }

    public TimeLogs(JsonObject obj) {
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

    public TimeLogs actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setConfirmed(Date confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed.getJson());
    }

    public Date getConfirmed() {
        try {
            if (_confirmed == null && SOURCE.has("confirmed") && SOURCE.get("confirmed") != null)
                _confirmed = Date.fromJson(SOURCE.getJsonObject("confirmed"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_confirmed != null && _confirmed.isSet())
            return _confirmed;

        return null;
    }

    public TimeLogs confirmed(Date confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed.getJson());
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

    public TimeLogs hours(Double hours) throws ParseException {
        _hours = hours;
        SOURCE.put("hours", hours);
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata != null && _metadata.isSet())
            return _metadata;

        return null;
    }

    public TimeLogs metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setOnmyway(OnMyWay onmyway) throws ParseException {
        _onmyway = onmyway;
        SOURCE.put("onmyway", onmyway.getJson());
    }

    public OnMyWay getOnmyway() {
        try {
            if (_onmyway == null && SOURCE.has("onmyway") && SOURCE.get("onmyway") != null)
                _onmyway = OnMyWay.fromJson(SOURCE.getJsonObject("onmyway"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_onmyway != null && _onmyway.isSet())
            return _onmyway;

        return null;
    }

    public TimeLogs onmyway(OnMyWay onmyway) throws ParseException {
        _onmyway = onmyway;
        SOURCE.put("onmyway", onmyway.getJson());
        return this;
    }

    public void setOpenTimeLog(TimeLog openTimeLog) throws ParseException {
        _openTimeLog = openTimeLog;
        SOURCE.put("open_time_log", openTimeLog.getJson());
    }

    public TimeLog getOpenTimeLog() {
        try {
            if (_openTimeLog == null && SOURCE.has("open_time_log") && SOURCE.get("open_time_log") != null)
                _openTimeLog = TimeLog.fromJson(SOURCE.getJsonObject("open_time_log"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_openTimeLog != null && _openTimeLog.isSet())
            return _openTimeLog;

        return null;
    }

    public TimeLogs openTimeLog(TimeLog openTimeLog) throws ParseException {
        _openTimeLog = openTimeLog;
        SOURCE.put("open_time_log", openTimeLog.getJson());
        return this;
    }

    public void setResults(TimeLog[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", TimeLog.toJsonArray(results));
    }

    public TimeLog[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = TimeLog.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public TimeLogs results(TimeLog[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", TimeLog.toJsonArray(results), true);
        return this;
    }

    public void setStatus(String status) throws ParseException {
        _status = status;
        SOURCE.put("status", status);
    }

    public String getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = SOURCE.getString("status");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public TimeLogs status(String status) throws ParseException {
        _status = status;
        SOURCE.put("status", status);
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

    public TimeLogs timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add"),
        @Json(name = "can_verify")
        CAN_VERIFY("can_verify"),
        @Json(name = "checkin")
        CHECKIN("checkin");

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
    public static JsonArray toJsonArray(TimeLogs[] array) {
        JsonArray list = new JsonArray();
        for (TimeLogs item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TimeLogs[] fromJsonArray(JsonArray array) {
        TimeLogs[] list = new TimeLogs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TimeLogs fromJson(JsonObject obj) {
        try {
            return new TimeLogs(obj);
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
    public static final Parcelable.Creator<TimeLogs> CREATOR = new Parcelable.Creator<TimeLogs>() {

        @Override
        public TimeLogs createFromParcel(Parcel source) {
            try {
                return TimeLogs.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TimeLogs[] newArray(int size) {
            return new TimeLogs[size];
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
        return true;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null && getActions() != null) {
            _actionsSet = new HashSet<>();
            if (getActions() != null) _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }
}

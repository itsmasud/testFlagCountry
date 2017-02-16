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

public class TimeLogs implements Parcelable {
    private static final String TAG = "TimeLogs";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "confirmed")
    private Date _confirmed;

    @Json(name = "correlation_id")
    private String _correlationId;

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

    @Json(name = "should_verify")
    private Boolean _shouldVerify;

    @Json(name = "status")
    private String _status;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public TimeLogs() {
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
        return _confirmed;
    }

    public TimeLogs confirmed(Date confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public TimeLogs correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setHours(Double hours) throws ParseException {
        _hours = hours;
        SOURCE.put("hours", hours);
    }

    public Double getHours() {
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
        return _metadata;
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
        return _onmyway;
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
        return _openTimeLog;
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
        return _results;
    }

    public TimeLogs results(TimeLog[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", TimeLog.toJsonArray(results), true);
        return this;
    }

    public void setShouldVerify(Boolean shouldVerify) throws ParseException {
        _shouldVerify = shouldVerify;
        SOURCE.put("should_verify", shouldVerify);
    }

    public Boolean getShouldVerify() {
        return _shouldVerify;
    }

    public TimeLogs shouldVerify(Boolean shouldVerify) throws ParseException {
        _shouldVerify = shouldVerify;
        SOURCE.put("should_verify", shouldVerify);
        return this;
    }

    public void setStatus(String status) throws ParseException {
        _status = status;
        SOURCE.put("status", status);
    }

    public String getStatus() {
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
        return _timeZone;
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
            return Unserializer.unserializeObject(TimeLogs.class, obj);
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
}

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

public class TimeLogs implements Parcelable {
    private static final String TAG = "TimeLogs";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "hours")
    private Double _hours;

    @Json(name = "should_verify")
    private Boolean _shouldVerify;

    @Json(name = "onmyway")
    private OnMyWay _onmyway;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "confirmed")
    private Date _confirmed;

    @Json(name = "actions")
    private ActionsEnum _actions;

    @Json(name = "results")
    private TimeLog[] _results;

    @Json(name = "status")
    private String _status;

    public TimeLogs() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public TimeLogs metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setHours(Double hours) {
        _hours = hours;
    }

    public Double getHours() {
        return _hours;
    }

    public TimeLogs hours(Double hours) {
        _hours = hours;
        return this;
    }

    public void setShouldVerify(Boolean shouldVerify) {
        _shouldVerify = shouldVerify;
    }

    public Boolean getShouldVerify() {
        return _shouldVerify;
    }

    public TimeLogs shouldVerify(Boolean shouldVerify) {
        _shouldVerify = shouldVerify;
        return this;
    }

    public void setOnmyway(OnMyWay onmyway) {
        _onmyway = onmyway;
    }

    public OnMyWay getOnmyway() {
        return _onmyway;
    }

    public TimeLogs onmyway(OnMyWay onmyway) {
        _onmyway = onmyway;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public TimeLogs timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setConfirmed(Date confirmed) {
        _confirmed = confirmed;
    }

    public Date getConfirmed() {
        return _confirmed;
    }

    public TimeLogs confirmed(Date confirmed) {
        _confirmed = confirmed;
        return this;
    }

    public void setActions(ActionsEnum actions) {
        _actions = actions;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public TimeLogs actions(ActionsEnum actions) {
        _actions = actions;
        return this;
    }

    public void setResults(TimeLog[] results) {
        _results = results;
    }

    public TimeLog[] getResults() {
        return _results;
    }

    public TimeLogs results(TimeLog[] results) {
        _results = results;
        return this;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public String getStatus() {
        return _status;
    }

    public TimeLogs status(String status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeLogs timeLogs) {
        try {
            return Serializer.serializeObject(timeLogs);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

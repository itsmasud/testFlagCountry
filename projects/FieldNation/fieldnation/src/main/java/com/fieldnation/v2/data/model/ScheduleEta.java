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

public class ScheduleEta implements Parcelable {
    private static final String TAG = "ScheduleEta";

    @Json(name = "end")
    private Date _end;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "mode")
    private Boolean _mode;

    @Json(name = "start")
    private Date _start;

    @Json(name = "status")
    private ScheduleEtaStatus _status;

    @Json(name = "user")
    private User _user;

    @Source
    private JsonObject SOURCE;

    public ScheduleEta() {
        SOURCE = new JsonObject();
    }

    public ScheduleEta(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEnd(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
    }

    public Date getEnd() {
        try {
            if (_end != null)
                return _end;

            if (SOURCE.has("end") && SOURCE.get("end") != null)
                _end = Date.fromJson(SOURCE.getJsonObject("end"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _end;
    }

    public ScheduleEta end(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
        return this;
    }

    public void setHourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
    }

    public Double getHourEstimate() {
        try {
            if (_hourEstimate != null)
                return _hourEstimate;

            if (SOURCE.has("hour_estimate") && SOURCE.get("hour_estimate") != null)
                _hourEstimate = SOURCE.getDouble("hour_estimate");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hourEstimate;
    }

    public ScheduleEta hourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
        return this;
    }

    public void setMode(Boolean mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
    }

    public Boolean getMode() {
        try {
            if (_mode != null)
                return _mode;

            if (SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = SOURCE.getBoolean("mode");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public ScheduleEta mode(Boolean mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
        return this;
    }

    public void setStart(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
    }

    public Date getStart() {
        try {
            if (_start != null)
                return _start;

            if (SOURCE.has("start") && SOURCE.get("start") != null)
                _start = Date.fromJson(SOURCE.getJsonObject("start"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _start;
    }

    public ScheduleEta start(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
        return this;
    }

    public void setStatus(ScheduleEtaStatus status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
    }

    public ScheduleEtaStatus getStatus() {
        try {
            if (_status != null)
                return _status;

            if (SOURCE.has("status") && SOURCE.get("status") != null)
                _status = ScheduleEtaStatus.fromJson(SOURCE.getJsonObject("status"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public ScheduleEta status(ScheduleEtaStatus status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        try {
            if (_user != null)
                return _user;

            if (SOURCE.has("user") && SOURCE.get("user") != null)
                _user = User.fromJson(SOURCE.getJsonObject("user"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _user;
    }

    public ScheduleEta user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ScheduleEta[] array) {
        JsonArray list = new JsonArray();
        for (ScheduleEta item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ScheduleEta[] fromJsonArray(JsonArray array) {
        ScheduleEta[] list = new ScheduleEta[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleEta fromJson(JsonObject obj) {
        try {
            return new ScheduleEta(obj);
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
    public static final Parcelable.Creator<ScheduleEta> CREATOR = new Parcelable.Creator<ScheduleEta>() {

        @Override
        public ScheduleEta createFromParcel(Parcel source) {
            try {
                return ScheduleEta.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleEta[] newArray(int size) {
            return new ScheduleEta[size];
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

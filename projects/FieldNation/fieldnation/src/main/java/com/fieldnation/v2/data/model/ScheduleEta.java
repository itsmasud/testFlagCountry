package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class ScheduleEta implements Parcelable {
    private static final String TAG = "ScheduleEta";

    @Json(name = "mode")
    private Boolean _mode;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "start")
    private Date _start;

    @Json(name = "end")
    private Date _end;

    @Json(name = "user")
    private User _user;

    // Need to add
    @Json(name = "notes")
    private String _notes;

    @Json(name = "status")
    private ScheduleEtaStatus _status;

    public ScheduleEta() {
    }

    public void setMode(Boolean mode) {
        _mode = mode;
    }

    public Boolean getMode() {
        return _mode;
    }

    public ScheduleEta mode(Boolean mode) {
        _mode = mode;
        return this;
    }

    public void setHourEstimate(Double hourEstimate) {
        _hourEstimate = hourEstimate;
    }

    public Double getHourEstimate() {
        return _hourEstimate;
    }

    public ScheduleEta hourEstimate(Double hourEstimate) {
        _hourEstimate = hourEstimate;
        return this;
    }

    public void setStart(Date start) {
        _start = start;
    }

    public Date getStart() {
        return _start;
    }

    public ScheduleEta start(Date start) {
        _start = start;
        return this;
    }

    public void setEnd(Date end) {
        _end = end;
    }

    public Date getEnd() {
        return _end;
    }

    public ScheduleEta end(Date end) {
        _end = end;
        return this;
    }

    public void setUser(User user) {
        _user = user;
    }

    public User getUser() {
        return _user;
    }

    public ScheduleEta user(User user) {
        _user = user;
        return this;
    }

    public void setStatus(ScheduleEtaStatus status) {
        _status = status;
    }

    public ScheduleEtaStatus getStatus() {
        return _status;
    }

    public ScheduleEta status(ScheduleEtaStatus status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleEta[] fromJsonArray(JsonArray array) {
        ScheduleEta[] list = new ScheduleEta[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleEta fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEta.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleEta scheduleEta) {
        try {
            return Serializer.serializeObject(scheduleEta);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

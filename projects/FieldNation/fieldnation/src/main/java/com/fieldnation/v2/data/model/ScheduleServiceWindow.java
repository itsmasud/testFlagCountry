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
 * Created by dmgen from swagger on 1/31/17.
 */

public class ScheduleServiceWindow implements Parcelable {
    private static final String TAG = "ScheduleServiceWindow";

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "start")
    private Date _start;

    @Json(name = "end")
    private Date _end;

    public ScheduleServiceWindow() {
    }

    public void setMode(ModeEnum mode) {
        _mode = mode;
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public ScheduleServiceWindow mode(ModeEnum mode) {
        _mode = mode;
        return this;
    }

    public void setStart(Date start) {
        _start = start;
    }

    public Date getStart() {
        return _start;
    }

    public ScheduleServiceWindow start(Date start) {
        _start = start;
        return this;
    }

    public void setEnd(Date end) {
        _end = end;
    }

    public Date getEnd() {
        return _end;
    }

    public ScheduleServiceWindow end(Date end) {
        _end = end;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleServiceWindow[] fromJsonArray(JsonArray array) {
        ScheduleServiceWindow[] list = new ScheduleServiceWindow[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleServiceWindow fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleServiceWindow.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleServiceWindow scheduleServiceWindow) {
        try {
            return Serializer.serializeObject(scheduleServiceWindow);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ScheduleServiceWindow> CREATOR = new Parcelable.Creator<ScheduleServiceWindow>() {

        @Override
        public ScheduleServiceWindow createFromParcel(Parcel source) {
            try {
                return ScheduleServiceWindow.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleServiceWindow[] newArray(int size) {
            return new ScheduleServiceWindow[size];
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

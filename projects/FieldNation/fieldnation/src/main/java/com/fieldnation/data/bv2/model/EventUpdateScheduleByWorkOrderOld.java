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

public class EventUpdateScheduleByWorkOrderOld implements Parcelable {
    private static final String TAG = "EventUpdateScheduleByWorkOrderOld";

    @Json(name = "mode")
    private String _mode;

    @Json(name = "start")
    private String _start;

    @Json(name = "end")
    private String _end;

    @Json(name = "time_zone")
    private String _timeZone;

    public EventUpdateScheduleByWorkOrderOld() {
    }

    public void setMode(String mode) {
        _mode = mode;
    }

    public String getMode() {
        return _mode;
    }

    public EventUpdateScheduleByWorkOrderOld mode(String mode) {
        _mode = mode;
        return this;
    }

    public void setStart(String start) {
        _start = start;
    }

    public String getStart() {
        return _start;
    }

    public EventUpdateScheduleByWorkOrderOld start(String start) {
        _start = start;
        return this;
    }

    public void setEnd(String end) {
        _end = end;
    }

    public String getEnd() {
        return _end;
    }

    public EventUpdateScheduleByWorkOrderOld end(String end) {
        _end = end;
        return this;
    }

    public void setTimeZone(String timeZone) {
        _timeZone = timeZone;
    }

    public String getTimeZone() {
        return _timeZone;
    }

    public EventUpdateScheduleByWorkOrderOld timeZone(String timeZone) {
        _timeZone = timeZone;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrderOld[] fromJsonArray(JsonArray array) {
        EventUpdateScheduleByWorkOrderOld[] list = new EventUpdateScheduleByWorkOrderOld[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrderOld fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrderOld.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EventUpdateScheduleByWorkOrderOld eventUpdateScheduleByWorkOrderOld) {
        try {
            return Serializer.serializeObject(eventUpdateScheduleByWorkOrderOld);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<EventUpdateScheduleByWorkOrderOld> CREATOR = new Parcelable.Creator<EventUpdateScheduleByWorkOrderOld>() {

        @Override
        public EventUpdateScheduleByWorkOrderOld createFromParcel(Parcel source) {
            try {
                return EventUpdateScheduleByWorkOrderOld.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EventUpdateScheduleByWorkOrderOld[] newArray(int size) {
            return new EventUpdateScheduleByWorkOrderOld[size];
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

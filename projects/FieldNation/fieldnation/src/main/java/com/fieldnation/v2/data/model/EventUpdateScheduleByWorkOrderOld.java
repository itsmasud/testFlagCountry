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

public class EventUpdateScheduleByWorkOrderOld implements Parcelable {
    private static final String TAG = "EventUpdateScheduleByWorkOrderOld";

    @Json(name = "end")
    private String _end;

    @Json(name = "mode")
    private String _mode;

    @Json(name = "start")
    private String _start;

    @Json(name = "time_zone")
    private String _timeZone;

    @Source
    private JsonObject SOURCE;

    public EventUpdateScheduleByWorkOrderOld() {
        SOURCE = new JsonObject();
    }

    public EventUpdateScheduleByWorkOrderOld(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEnd(String end) throws ParseException {
        _end = end;
        SOURCE.put("end", end);
    }

    public String getEnd() {
        try {
            if (_end != null)
                return _end;

            if (SOURCE.has("end") && SOURCE.get("end") != null)
                _end = SOURCE.getString("end");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _end;
    }

    public EventUpdateScheduleByWorkOrderOld end(String end) throws ParseException {
        _end = end;
        SOURCE.put("end", end);
        return this;
    }

    public void setMode(String mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
    }

    public String getMode() {
        try {
            if (_mode != null)
                return _mode;

            if (SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = SOURCE.getString("mode");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public EventUpdateScheduleByWorkOrderOld mode(String mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
        return this;
    }

    public void setStart(String start) throws ParseException {
        _start = start;
        SOURCE.put("start", start);
    }

    public String getStart() {
        try {
            if (_start != null)
                return _start;

            if (SOURCE.has("start") && SOURCE.get("start") != null)
                _start = SOURCE.getString("start");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _start;
    }

    public EventUpdateScheduleByWorkOrderOld start(String start) throws ParseException {
        _start = start;
        SOURCE.put("start", start);
        return this;
    }

    public void setTimeZone(String timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone);
    }

    public String getTimeZone() {
        try {
            if (_timeZone != null)
                return _timeZone;

            if (SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = SOURCE.getString("time_zone");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeZone;
    }

    public EventUpdateScheduleByWorkOrderOld timeZone(String timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EventUpdateScheduleByWorkOrderOld[] array) {
        JsonArray list = new JsonArray();
        for (EventUpdateScheduleByWorkOrderOld item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrderOld[] fromJsonArray(JsonArray array) {
        EventUpdateScheduleByWorkOrderOld[] list = new EventUpdateScheduleByWorkOrderOld[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrderOld fromJson(JsonObject obj) {
        try {
            return new EventUpdateScheduleByWorkOrderOld(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}

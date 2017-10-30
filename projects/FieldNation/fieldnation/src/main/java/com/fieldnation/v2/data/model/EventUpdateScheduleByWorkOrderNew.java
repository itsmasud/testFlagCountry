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

public class EventUpdateScheduleByWorkOrderNew implements Parcelable {
    private static final String TAG = "EventUpdateScheduleByWorkOrderNew";

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

    public EventUpdateScheduleByWorkOrderNew() {
        SOURCE = new JsonObject();
    }

    public EventUpdateScheduleByWorkOrderNew(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEnd(String end) throws ParseException {
        _end = end;
        SOURCE.put("end", end);
    }

    public String getEnd() {
        try {
            if (_end == null && SOURCE.has("end") && SOURCE.get("end") != null)
                _end = SOURCE.getString("end");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _end;
    }

    public EventUpdateScheduleByWorkOrderNew end(String end) throws ParseException {
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
            if (_mode == null && SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = SOURCE.getString("mode");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public EventUpdateScheduleByWorkOrderNew mode(String mode) throws ParseException {
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
            if (_start == null && SOURCE.has("start") && SOURCE.get("start") != null)
                _start = SOURCE.getString("start");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _start;
    }

    public EventUpdateScheduleByWorkOrderNew start(String start) throws ParseException {
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
            if (_timeZone == null && SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = SOURCE.getString("time_zone");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeZone;
    }

    public EventUpdateScheduleByWorkOrderNew timeZone(String timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EventUpdateScheduleByWorkOrderNew[] array) {
        JsonArray list = new JsonArray();
        for (EventUpdateScheduleByWorkOrderNew item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrderNew[] fromJsonArray(JsonArray array) {
        EventUpdateScheduleByWorkOrderNew[] list = new EventUpdateScheduleByWorkOrderNew[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrderNew fromJson(JsonObject obj) {
        try {
            return new EventUpdateScheduleByWorkOrderNew(obj);
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
    public static final Parcelable.Creator<EventUpdateScheduleByWorkOrderNew> CREATOR = new Parcelable.Creator<EventUpdateScheduleByWorkOrderNew>() {

        @Override
        public EventUpdateScheduleByWorkOrderNew createFromParcel(Parcel source) {
            try {
                return EventUpdateScheduleByWorkOrderNew.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EventUpdateScheduleByWorkOrderNew[] newArray(int size) {
            return new EventUpdateScheduleByWorkOrderNew[size];
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

}

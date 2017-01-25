package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EventUpdateScheduleByWorkOrderNew {
    private static final String TAG = "EventUpdateScheduleByWorkOrderNew";

    @Json(name = "mode")
    private String _mode;

    @Json(name = "start")
    private String _start;

    @Json(name = "end")
    private String _end;

    @Json(name = "time_zone")
    private String _timeZone;

    public EventUpdateScheduleByWorkOrderNew() {
    }

    public String getMode() {
        return _mode;
    }

    public String getStart() {
        return _start;
    }

    public String getEnd() {
        return _end;
    }

    public String getTimeZone() {
        return _timeZone;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrderNew fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrderNew.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EventUpdateScheduleByWorkOrderNew eventUpdateScheduleByWorkOrderNew) {
        try {
            return Serializer.serializeObject(eventUpdateScheduleByWorkOrderNew);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

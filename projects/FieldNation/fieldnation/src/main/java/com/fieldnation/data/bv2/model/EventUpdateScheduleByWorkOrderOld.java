package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EventUpdateScheduleByWorkOrderOld {
    private static final String TAG = "EventUpdateScheduleByWorkOrderOld";

    @Json(name = "mode")
    private String mode = null;

    @Json(name = "start")
    private String start = null;

    @Json(name = "end")
    private String end = null;

    @Json(name = "time_zone")
    private String timeZone = null;

    public EventUpdateScheduleByWorkOrderOld() {
    }

    public String getMode() {
        return mode;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getTimeZone() {
        return timeZone;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrderOld fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrderOld.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}


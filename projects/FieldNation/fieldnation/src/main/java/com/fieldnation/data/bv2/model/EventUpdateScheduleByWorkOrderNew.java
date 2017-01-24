package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EventUpdateScheduleByWorkOrderNew {
    private static final String TAG = "EventUpdateScheduleByWorkOrderNew";

    @Json(name = "mode")
    private String mode;

    @Json(name = "start")
    private String start;

    @Json(name = "end")
    private String end;

    @Json(name = "time_zone")
    private String timeZone;

    public EventUpdateScheduleByWorkOrderNew() {
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
    public static EventUpdateScheduleByWorkOrderNew fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrderNew.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

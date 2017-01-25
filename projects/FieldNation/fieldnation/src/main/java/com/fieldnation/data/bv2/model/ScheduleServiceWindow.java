package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleServiceWindow {
    private static final String TAG = "ScheduleServiceWindow";

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "start")
    private Date _start;

    @Json(name = "end")
    private Date _end;

    public ScheduleServiceWindow() {
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public Date getStart() {
        return _start;
    }

    public Date getEnd() {
        return _end;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}

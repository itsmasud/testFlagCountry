package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleServiceWindow {
    private static final String TAG = "ScheduleServiceWindow";

    @Json(name = "start")
    private String start = null;

    @Json(name = "end")
    private String end = null;

    @Json(name = "mode")
    private ModeEnum mode = null;

    public enum ModeEnum {
        @Json(name = "hours")
        HOURS("hours"),
        @Json(name = "between")
        BETWEEN("between"),
        @Json(name = "exact")
        EXACT("exact");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public ScheduleServiceWindow() {
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public ModeEnum getMode() {
        return mode;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleServiceWindow fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleServiceWindow.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
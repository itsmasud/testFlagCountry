package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleEtaStatus {
    public static final String TAG = "ScheduleEtaStatus";

    @Json(name = "name")
    private NameEnum name = null;

    @Json(name = "updated")
    private String updated = null;

    public enum NameEnum {
        @Json(name = "unconfirmed")
        UNCONFIRMED("unconfirmed"),
        @Json(name = "confirmed")
        CONFIRMED("confirmed"),
        @Json(name = "readytogo")
        READYTOGO("readytogo"),
        @Json(name = "onmyway")
        ONMYWAY("onmyway");

        private String value;

        NameEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public ScheduleEtaStatus() {
    }

    public NameEnum getName() {
        return name;
    }

    public String getUpdated() {
        return updated;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleEtaStatus fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEtaStatus.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleEtaStatus scheduleEtaStatus) {
        try {
            return Serializer.serializeObject(scheduleEtaStatus);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
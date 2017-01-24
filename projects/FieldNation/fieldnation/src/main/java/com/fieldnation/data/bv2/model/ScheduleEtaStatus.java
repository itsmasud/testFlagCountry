package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ScheduleEtaStatus {
    private static final String TAG = "ScheduleEtaStatus";

    @Json(name = "name")
    private NameEnum name;

    @Json(name = "updated")
    private Date updated;

    public ScheduleEtaStatus() {
    }

    public NameEnum getName() {
        return name;
    }

    public Date getUpdated() {
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

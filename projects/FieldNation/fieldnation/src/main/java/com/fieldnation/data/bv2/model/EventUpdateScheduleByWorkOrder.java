package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EventUpdateScheduleByWorkOrder {
    private static final String TAG = "EventUpdateScheduleByWorkOrder";

    @Json(name = "old")
    private EventUpdateScheduleByWorkOrderOld old = null;

    @Json(name = "new")
    private EventUpdateScheduleByWorkOrderOld _new = null;

    public EventUpdateScheduleByWorkOrder() {
    }

    public EventUpdateScheduleByWorkOrderOld getOld() {
        return old;
    }

    public EventUpdateScheduleByWorkOrderOld getNew() {
        return _new;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EventUpdateScheduleByWorkOrder eventUpdateScheduleByWorkOrder) {
        try {
            return Serializer.serializeObject(eventUpdateScheduleByWorkOrder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}


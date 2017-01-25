package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EventUpdateScheduleByWorkOrder {
    private static final String TAG = "EventUpdateScheduleByWorkOrder";

    @Json(name = "new")
    private EventUpdateScheduleByWorkOrderNew _new;

    @Json(name = "old")
    private EventUpdateScheduleByWorkOrderOld _old;

    public EventUpdateScheduleByWorkOrder() {
    }

    public EventUpdateScheduleByWorkOrderNew getNew() {
        return _new;
    }

    public EventUpdateScheduleByWorkOrderOld getOld() {
        return _old;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EventUpdateScheduleByWorkOrder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

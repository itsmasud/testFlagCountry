package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class EventUpdateScheduleByWorkOrder implements Parcelable {
    private static final String TAG = "EventUpdateScheduleByWorkOrder";

    @Json(name = "new")
    private EventUpdateScheduleByWorkOrderNew _new;

    @Json(name = "old")
    private EventUpdateScheduleByWorkOrderOld _old;

    public EventUpdateScheduleByWorkOrder() {
    }

    public void setNew(EventUpdateScheduleByWorkOrderNew neww) {
        _new = neww;
    }

    public EventUpdateScheduleByWorkOrderNew getNew() {
        return _new;
    }

    public EventUpdateScheduleByWorkOrder neww(EventUpdateScheduleByWorkOrderNew neww) {
        _new = neww;
        return this;
    }

    public void setOld(EventUpdateScheduleByWorkOrderOld old) {
        _old = old;
    }

    public EventUpdateScheduleByWorkOrderOld getOld() {
        return _old;
    }

    public EventUpdateScheduleByWorkOrder old(EventUpdateScheduleByWorkOrderOld old) {
        _old = old;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EventUpdateScheduleByWorkOrder[] fromJsonArray(JsonArray array) {
        EventUpdateScheduleByWorkOrder[] list = new EventUpdateScheduleByWorkOrder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<EventUpdateScheduleByWorkOrder> CREATOR = new Parcelable.Creator<EventUpdateScheduleByWorkOrder>() {

        @Override
        public EventUpdateScheduleByWorkOrder createFromParcel(Parcel source) {
            try {
                return EventUpdateScheduleByWorkOrder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EventUpdateScheduleByWorkOrder[] newArray(int size) {
            return new EventUpdateScheduleByWorkOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}

package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class EventUpdateScheduleByWorkOrder implements Parcelable {
    private static final String TAG = "EventUpdateScheduleByWorkOrder";

    @Json(name = "new")
    private EventUpdateScheduleByWorkOrderNew _new;

    @Json(name = "old")
    private EventUpdateScheduleByWorkOrderOld _old;

    @Source
    private JsonObject SOURCE;

    public EventUpdateScheduleByWorkOrder() {
        SOURCE = new JsonObject();
    }

    public EventUpdateScheduleByWorkOrder(JsonObject obj) {
        SOURCE = obj;
    }

    public void setNew(EventUpdateScheduleByWorkOrderNew neww) throws ParseException {
        _new = neww;
        SOURCE.put("new", neww.getJson());
    }

    public EventUpdateScheduleByWorkOrderNew getNew() {
        try {
            if (_new == null && SOURCE.has("new") && SOURCE.get("new") != null)
                _new = EventUpdateScheduleByWorkOrderNew.fromJson(SOURCE.getJsonObject("new"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_new == null)
            _new = new EventUpdateScheduleByWorkOrderNew();

        return _new;
    }

    public EventUpdateScheduleByWorkOrder neww(EventUpdateScheduleByWorkOrderNew neww) throws ParseException {
        _new = neww;
        SOURCE.put("new", neww.getJson());
        return this;
    }

    public void setOld(EventUpdateScheduleByWorkOrderOld old) throws ParseException {
        _old = old;
        SOURCE.put("old", old.getJson());
    }

    public EventUpdateScheduleByWorkOrderOld getOld() {
        try {
            if (_old == null && SOURCE.has("old") && SOURCE.get("old") != null)
                _old = EventUpdateScheduleByWorkOrderOld.fromJson(SOURCE.getJsonObject("old"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_old == null)
            _old = new EventUpdateScheduleByWorkOrderOld();

        return _old;
    }

    public EventUpdateScheduleByWorkOrder old(EventUpdateScheduleByWorkOrderOld old) throws ParseException {
        _old = old;
        SOURCE.put("old", old.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EventUpdateScheduleByWorkOrder[] array) {
        JsonArray list = new JsonArray();
        for (EventUpdateScheduleByWorkOrder item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrder[] fromJsonArray(JsonArray array) {
        EventUpdateScheduleByWorkOrder[] list = new EventUpdateScheduleByWorkOrder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EventUpdateScheduleByWorkOrder fromJson(JsonObject obj) {
        try {
            return new EventUpdateScheduleByWorkOrder(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}

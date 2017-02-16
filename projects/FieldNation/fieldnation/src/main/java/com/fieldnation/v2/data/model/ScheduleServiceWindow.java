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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class ScheduleServiceWindow implements Parcelable {
    private static final String TAG = "ScheduleServiceWindow";

    @Json(name = "end")
    private Date _end;

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "start")
    private Date _start;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ScheduleServiceWindow() {
    }

    public void setEnd(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
    }

    public Date getEnd() {
        return _end;
    }

    public ScheduleServiceWindow end(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
        return this;
    }

    public void setMode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public ScheduleServiceWindow mode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
        return this;
    }

    public void setStart(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
    }

    public Date getStart() {
        return _start;
    }

    public ScheduleServiceWindow start(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ModeEnum {
        @Json(name = "between")
        BETWEEN("between"),
        @Json(name = "exact")
        EXACT("exact"),
        @Json(name = "hours")
        HOURS("hours");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ScheduleServiceWindow[] array) {
        JsonArray list = new JsonArray();
        for (ScheduleServiceWindow item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ScheduleServiceWindow[] fromJsonArray(JsonArray array) {
        ScheduleServiceWindow[] list = new ScheduleServiceWindow[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleServiceWindow fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleServiceWindow.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ScheduleServiceWindow> CREATOR = new Parcelable.Creator<ScheduleServiceWindow>() {

        @Override
        public ScheduleServiceWindow createFromParcel(Parcel source) {
            try {
                return ScheduleServiceWindow.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleServiceWindow[] newArray(int size) {
            return new ScheduleServiceWindow[size];
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
}

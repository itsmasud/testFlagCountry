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

public class Local implements Parcelable {
    private static final String TAG = "Local";

    @Json(name = "date")
    private String _date;

    @Json(name = "time")
    private String _time;

    @Source
    private JsonObject SOURCE;

    public Local() {
        SOURCE = new JsonObject();
    }

    public Local(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDate(String date) throws ParseException {
        _date = date;
        SOURCE.put("date", date);
    }

    public String getDate() {
        try {
            if (_date == null && SOURCE.has("date") && SOURCE.get("date") != null)
            _date = SOURCE.getString("date");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _date;
    }

    public Local date(String date) throws ParseException {
        _date = date;
        SOURCE.put("date", date);
        return this;
    }

    public void setTime(String time) throws ParseException {
        _time = time;
        SOURCE.put("time", time);
    }

    public String getTime() {
        try {
            if (_time == null && SOURCE.has("time") && SOURCE.get("time") != null)
                _time = SOURCE.getString("time");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _time;
    }

    public Local time(String time) throws ParseException {
        _time = time;
        SOURCE.put("time", time);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Local[] array) {
        JsonArray list = new JsonArray();
        for (Local item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Local[] fromJsonArray(JsonArray array) {
        Local[] list = new Local[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Local fromJson(JsonObject obj) {
        try {
            return new Local(obj);
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
    public static final Parcelable.Creator<Local> CREATOR = new Parcelable.Creator<Local>() {

        @Override
        public Local createFromParcel(Parcel source) {
            try {
                return Local.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
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

    public boolean isSet() {
        return true;
    }
}

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

public class ListEnvelopeTimings implements Parcelable {
    private static final String TAG = "ListEnvelopeTimings";

    @Json(name = "name")
    private String _name;

    @Json(name = "time")
    private Integer _time;

    @Source
    private JsonObject SOURCE;

    public ListEnvelopeTimings() {
        SOURCE = new JsonObject();
    }

    public ListEnvelopeTimings(JsonObject obj) {
        SOURCE = obj;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public ListEnvelopeTimings name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setTime(Integer time) throws ParseException {
        _time = time;
        SOURCE.put("time", time);
    }

    public Integer getTime() {
        try {
            if (_time == null && SOURCE.has("time") && SOURCE.get("time") != null)
                _time = SOURCE.getInt("time");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _time;
    }

    public ListEnvelopeTimings time(Integer time) throws ParseException {
        _time = time;
        SOURCE.put("time", time);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ListEnvelopeTimings[] array) {
        JsonArray list = new JsonArray();
        for (ListEnvelopeTimings item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ListEnvelopeTimings[] fromJsonArray(JsonArray array) {
        ListEnvelopeTimings[] list = new ListEnvelopeTimings[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ListEnvelopeTimings fromJson(JsonObject obj) {
        try {
            return new ListEnvelopeTimings(obj);
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
    public static final Parcelable.Creator<ListEnvelopeTimings> CREATOR = new Parcelable.Creator<ListEnvelopeTimings>() {

        @Override
        public ListEnvelopeTimings createFromParcel(Parcel source) {
            try {
                return ListEnvelopeTimings.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ListEnvelopeTimings[] newArray(int size) {
            return new ListEnvelopeTimings[size];
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

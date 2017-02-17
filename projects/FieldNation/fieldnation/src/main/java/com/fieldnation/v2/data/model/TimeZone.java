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

public class TimeZone implements Parcelable {
    private static final String TAG = "TimeZone";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "offset")
    private Double _offset;

    @Json(name = "short")
    private String _short;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public TimeZone() {
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public TimeZone id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public TimeZone name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOffset(Double offset) throws ParseException {
        _offset = offset;
        SOURCE.put("offset", offset);
    }

    public Double getOffset() {
        return _offset;
    }

    public TimeZone offset(Double offset) throws ParseException {
        _offset = offset;
        SOURCE.put("offset", offset);
        return this;
    }

    public void setShort(String shortt) throws ParseException {
        _short = shortt;
        SOURCE.put("short", shortt);
    }

    public String getShort() {
        return _short;
    }

    public TimeZone shortt(String shortt) throws ParseException {
        _short = shortt;
        SOURCE.put("short", shortt);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TimeZone[] array) {
        JsonArray list = new JsonArray();
        for (TimeZone item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TimeZone[] fromJsonArray(JsonArray array) {
        TimeZone[] list = new TimeZone[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TimeZone fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TimeZone.class, obj);
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
    public static final Parcelable.Creator<TimeZone> CREATOR = new Parcelable.Creator<TimeZone>() {

        @Override
        public TimeZone createFromParcel(Parcel source) {
            try {
                return TimeZone.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TimeZone[] newArray(int size) {
            return new TimeZone[size];
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

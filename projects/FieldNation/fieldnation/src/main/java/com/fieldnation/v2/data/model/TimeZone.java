package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
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
    private JsonObject SOURCE;

    public TimeZone() {
        SOURCE = new JsonObject();
    }

    public TimeZone(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_offset == null && SOURCE.has("offset") && SOURCE.get("offset") != null)
                _offset = SOURCE.getDouble("offset");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_short == null && SOURCE.has("short") && SOURCE.get("short") != null)
                _short = SOURCE.getString("short");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
            return new TimeZone(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}

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

public class TimeZone implements Parcelable {
    private static final String TAG = "TimeZone";

    @Json(name = "offset")
    private Double _offset;

    @Json(name = "name")
    private String _name;

    @Json(name = "short")
    private String _short;

    @Json(name = "id")
    private Integer _id;

    public TimeZone() {
    }

    public void setOffset(Double offset) {
        _offset = offset;
    }

    public Double getOffset() {
        return _offset;
    }

    public TimeZone offset(Double offset) {
        _offset = offset;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public TimeZone name(String name) {
        _name = name;
        return this;
    }

    public void setShort(String shortt) {
        _short = shortt;
    }

    public String getShort() {
        return _short;
    }

    public TimeZone shortt(String shortt) {
        _short = shortt;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public TimeZone id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TimeZone timeZone) {
        try {
            return Serializer.serializeObject(timeZone);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}

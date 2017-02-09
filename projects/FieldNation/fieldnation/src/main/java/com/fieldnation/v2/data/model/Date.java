package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by dmgen from swagger.
 */

public class Date implements Parcelable {
    private static final String TAG = "Date";

    @Json(name = "local") // Don't use if you can, this should be in local time zone
    private Local _local;
    @Json(name = "utc") // 2017-01-30 00:00:00
    private String _utc;

    public Date() {
    }

/*
    public void setLocal(Local local) {
        _local = local;
    }

    public Local getLocal() {
        return _local;
    }

    public Date local(Local local) {
        _local = local;
        return this;
    }
*/

    public void setUtc(String utc) {
        _utc = utc;
    }

    public String getUtcString() {
        return _utc;
    }

    public Date utc(String utc) {
        _utc = utc;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Date[] fromJsonArray(JsonArray array) {
        Date[] list = new Date[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Date fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Date.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Date date) {
        try {
            return Serializer.serializeObject(date);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Date> CREATOR = new Parcelable.Creator<Date>() {

        @Override
        public Date createFromParcel(Parcel source) {
            try {
                return Date.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
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


    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    public Date(Calendar calendar) {
        super();
        utc(DateUtils.v2CalToUtc(calendar));
    }

    public Date(long utcMilliseconds) {
        super();
        utc(DateUtils.v2LongToUtc(utcMilliseconds));
    }

    public Calendar getCalendar() throws ParseException {
        return DateUtils.v2UtcToCalendar(_utc);
    }

    public long getUtc() throws ParseException {
        return DateUtils.v2UtcToLong(_utc);
    }
}

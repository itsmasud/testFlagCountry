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
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by dmgen from swagger.
 */

public class Date implements Parcelable {
    private static final String TAG = "Date";

/*
    @Json(name = "local") // Don't use if you can, this should be in local time zone
    private Local _local;
*/

    @Json(name = "utc") // 2017-01-30 00:00:00
    private String _utc;

    @Source
    private JsonObject SOURCE;

    public Date() {
        SOURCE = new JsonObject();
    }

    public Date(JsonObject obj) {
        SOURCE = obj;
    }

/*
    public void setLocal(Local local) throws ParseException {
        _local = local;
        SOURCE.put("local", local.getJson());
    }

    public Local getLocal() {
        try {
            if (_local == null && SOURCE.has("local") && SOURCE.get("local") != null)
            _local = Local.fromJson(SOURCE.getJsonObject("local"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_local != null && _local.isSet())
        return _local;

        return null;
    }

    public Date local(Local local) throws ParseException {
        _local = local;
        SOURCE.put("local", local.getJson());
        return this;
    }
*/

    public void setUtc(String utc) throws ParseException {
        _utc = utc;
        SOURCE.put("utc", utc);
    }

    public String getUtc() {
        try {
            if (_utc == null && SOURCE.has("utc") && SOURCE.get("utc") != null)
                _utc = SOURCE.getString("utc");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (!misc.isEmptyOrNull(_utc))
            return _utc;

        return null;
    }

    public Date utc(String utc) throws ParseException {
        _utc = utc;
        SOURCE.put("utc", utc);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Date[] array) {
        JsonArray list = new JsonArray();
        for (Date item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Date[] fromJsonArray(JsonArray array) {
        Date[] list = new Date[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Date fromJson(JsonObject obj) {
        try {
            return new Date(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return !misc.isEmptyOrNull(getUtc());
    }

    public Date(Calendar calendar) throws ParseException {
        this();
        utc(DateUtils.v2CalToUtc(calendar));
    }

    public Date(long utcMilliseconds) throws ParseException {
        this();
        utc(DateUtils.v2LongToUtc(utcMilliseconds));
    }

    public Calendar getCalendar() throws ParseException {
        return DateUtils.v2UtcToCalendar(getUtc());
    }

    public long getUtcLong() throws ParseException {
        return DateUtils.v2UtcToLong(getUtc());
    }
}

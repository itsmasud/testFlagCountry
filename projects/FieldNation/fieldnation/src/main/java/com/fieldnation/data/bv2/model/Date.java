package com.fieldnation.data.bv2.model;

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

public class Date implements Parcelable {
    private static final String TAG = "Date";

    @Json(name = "utc")
    private Long _utc;

    @Json(name = "local")
    private Local _local;

    public Date() {
    }

    public void setUtc(Long utc) {
        _utc = utc;
    }

    public Long getUtc() {
        return _utc;
    }

    public Date utc(Long utc) {
        _utc = utc;
        return this;
    }

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
}

package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class CheckInOutTimeLog implements Parcelable {
    private static final String TAG = "CheckInOutTimeLog";

    @Json(name = "id")
    private Integer _id;

    public CheckInOutTimeLog() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public CheckInOutTimeLog id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CheckInOutTimeLog fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CheckInOutTimeLog.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CheckInOutTimeLog checkInOutTimeLog) {
        try {
            return Serializer.serializeObject(checkInOutTimeLog);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CheckInOutTimeLog> CREATOR = new Parcelable.Creator<CheckInOutTimeLog>() {

        @Override
        public CheckInOutTimeLog createFromParcel(Parcel source) {
            try {
                return CheckInOutTimeLog.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CheckInOutTimeLog[] newArray(int size) {
            return new CheckInOutTimeLog[size];
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

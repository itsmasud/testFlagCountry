package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UserUpcomingSchedule implements Parcelable {
    private static final String TAG = "UserUpcomingSchedule";

    @Json(name = "date")
    private String _date;

    @Json(name = "from")
    private String _from;

    @Json(name = "to")
    private String _to;

    public UserUpcomingSchedule() {
    }

    public void setDate(String date) {
        _date = date;
    }

    public String getDate() {
        return _date;
    }

    public UserUpcomingSchedule date(String date) {
        _date = date;
        return this;
    }

    public void setFrom(String from) {
        _from = from;
    }

    public String getFrom() {
        return _from;
    }

    public UserUpcomingSchedule from(String from) {
        _from = from;
        return this;
    }

    public void setTo(String to) {
        _to = to;
    }

    public String getTo() {
        return _to;
    }

    public UserUpcomingSchedule to(String to) {
        _to = to;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserUpcomingSchedule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserUpcomingSchedule.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserUpcomingSchedule userUpcomingSchedule) {
        try {
            return Serializer.serializeObject(userUpcomingSchedule);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserUpcomingSchedule> CREATOR = new Parcelable.Creator<UserUpcomingSchedule>() {

        @Override
        public UserUpcomingSchedule createFromParcel(Parcel source) {
            try {
                return UserUpcomingSchedule.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserUpcomingSchedule[] newArray(int size) {
            return new UserUpcomingSchedule[size];
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

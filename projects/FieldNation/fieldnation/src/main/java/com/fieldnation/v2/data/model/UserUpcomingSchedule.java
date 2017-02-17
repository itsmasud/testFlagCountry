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

public class UserUpcomingSchedule implements Parcelable {
    private static final String TAG = "UserUpcomingSchedule";

    @Json(name = "date")
    private String _date;

    @Json(name = "from")
    private String _from;

    @Json(name = "to")
    private String _to;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserUpcomingSchedule() {
    }

    public void setDate(String date) throws ParseException {
        _date = date;
        SOURCE.put("date", date);
    }

    public String getDate() {
        return _date;
    }

    public UserUpcomingSchedule date(String date) throws ParseException {
        _date = date;
        SOURCE.put("date", date);
        return this;
    }

    public void setFrom(String from) throws ParseException {
        _from = from;
        SOURCE.put("from", from);
    }

    public String getFrom() {
        return _from;
    }

    public UserUpcomingSchedule from(String from) throws ParseException {
        _from = from;
        SOURCE.put("from", from);
        return this;
    }

    public void setTo(String to) throws ParseException {
        _to = to;
        SOURCE.put("to", to);
    }

    public String getTo() {
        return _to;
    }

    public UserUpcomingSchedule to(String to) throws ParseException {
        _to = to;
        SOURCE.put("to", to);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserUpcomingSchedule[] array) {
        JsonArray list = new JsonArray();
        for (UserUpcomingSchedule item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserUpcomingSchedule[] fromJsonArray(JsonArray array) {
        UserUpcomingSchedule[] list = new UserUpcomingSchedule[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserUpcomingSchedule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserUpcomingSchedule.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}

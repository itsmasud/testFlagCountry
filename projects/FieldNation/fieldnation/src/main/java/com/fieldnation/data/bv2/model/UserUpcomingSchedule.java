package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserUpcomingSchedule {
    private static final String TAG = "UserUpcomingSchedule";

    @Json(name = "date")
    private String _date;

    @Json(name = "from")
    private String _from;

    @Json(name = "to")
    private String _to;

    public UserUpcomingSchedule() {
    }

    public String getDate() {
        return _date;
    }

    public String getFrom() {
        return _from;
    }

    public String getTo() {
        return _to;
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
}

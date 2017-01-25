package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CheckInOut {
    private static final String TAG = "CheckInOut";

    @Json(name = "time_log")
    private CheckInOutTimeLog _timeLog;

    @Json(name = "actor")
    private User _actor;

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "created")
    private Date _created;

    @Json(name = "verified")
    private Boolean _verified;

    @Json(name = "distance_from_check_in")
    private Double _distanceFromCheckIn;

    @Json(name = "coords")
    private Coords _coords;

    public CheckInOut() {
    }

    public CheckInOutTimeLog getTimeLog() {
        return _timeLog;
    }

    public User getActor() {
        return _actor;
    }

    public Double getDistance() {
        return _distance;
    }

    public Date getCreated() {
        return _created;
    }

    public Boolean getVerified() {
        return _verified;
    }

    public Double getDistanceFromCheckIn() {
        return _distanceFromCheckIn;
    }

    public Coords getCoords() {
        return _coords;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CheckInOut fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CheckInOut.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CheckInOut checkInOut) {
        try {
            return Serializer.serializeObject(checkInOut);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

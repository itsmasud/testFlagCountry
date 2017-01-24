package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CheckInOut {
    private static final String TAG = "CheckInOut";

    @Json(name = "time_log")
    private CheckInOutTimeLog timeLog;

    @Json(name = "actor")
    private User actor;

    @Json(name = "distance")
    private Double distance;

    @Json(name = "created")
    private Date created;

    @Json(name = "verified")
    private Boolean verified;

    @Json(name = "distance_from_check_in")
    private Double distanceFromCheckIn;

    @Json(name = "coords")
    private Coords coords;

    public CheckInOut() {
    }

    public CheckInOutTimeLog getTimeLog() {
        return timeLog;
    }

    public User getActor() {
        return actor;
    }

    public Double getDistance() {
        return distance;
    }

    public Date getCreated() {
        return created;
    }

    public Boolean getVerified() {
        return verified;
    }

    public Double getDistanceFromCheckIn() {
        return distanceFromCheckIn;
    }

    public Coords getCoords() {
        return coords;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CheckInOut fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CheckInOut.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

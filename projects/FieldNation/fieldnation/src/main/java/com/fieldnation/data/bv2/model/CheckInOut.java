package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CheckInOut {
    private static final String TAG = "CheckInOut";

    @Json(name = "time_log")
    private InlineResponse201 timeLog = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "actor")
    private User actor = null;

    @Json(name = "coords")
    private Coords coords = null;

    @Json(name = "distance")
    private Double distance = null;

    @Json(name = "distance_from_check_in")
    private Double distanceFromCheckIn = null;

    @Json(name = "verified")
    private Boolean verified = null;

    public CheckInOut() {
    }

    public InlineResponse201 getTimeLog() {
        return timeLog;
    }

    public String getCreated() {
        return created;
    }

    public User getActor() {
        return actor;
    }

    public Coords getCoords() {
        return coords;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getDistanceFromCheckIn() {
        return distanceFromCheckIn;
    }

    public Boolean getVerified() {
        return verified;
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
package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class OnMyWay {
    private static final String TAG = "OnMyWay";

    @Json(name = "active")
    private Boolean active = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "drive_time")
    private Integer driveTime = null;

    @Json(name = "distance")
    private Double distance = null;

    @Json(name = "status")
    private String status = null;

    @Json(name = "substatus")
    private String substatus = null;

    @Json(name = "estimated_delay")
    private Integer estimatedDelay = null;

    @Json(name = "coords")
    private Coords coords = null;

    public OnMyWay() {
    }

    public Boolean getActive() {
        return active;
    }

    public String getCreated() {
        return created;
    }

    public Integer getDriveTime() {
        return driveTime;
    }

    public Double getDistance() {
        return distance;
    }

    public String getStatus() {
        return status;
    }

    public String getSubstatus() {
        return substatus;
    }

    public Integer getEstimatedDelay() {
        return estimatedDelay;
    }

    public Coords getCoords() {
        return coords;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static OnMyWay fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(OnMyWay.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(OnMyWay onMyWay) {
        try {
            return Serializer.serializeObject(onMyWay);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
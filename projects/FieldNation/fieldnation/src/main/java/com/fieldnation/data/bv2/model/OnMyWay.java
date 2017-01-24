package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class OnMyWay {
    private static final String TAG = "OnMyWay";

    @Json(name = "distance")
    private Double distance;

    @Json(name = "substatus")
    private String substatus;

    @Json(name = "created")
    private Date created;

    @Json(name = "estimated_delay")
    private Integer estimatedDelay;

    @Json(name = "active")
    private Boolean active;

    @Json(name = "drive_time")
    private Integer driveTime;

    @Json(name = "coords")
    private Coords coords;

    @Json(name = "status")
    private String status;

    public OnMyWay() {
    }

    public Double getDistance() {
        return distance;
    }

    public String getSubstatus() {
        return substatus;
    }

    public Date getCreated() {
        return created;
    }

    public Integer getEstimatedDelay() {
        return estimatedDelay;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getDriveTime() {
        return driveTime;
    }

    public Coords getCoords() {
        return coords;
    }

    public String getStatus() {
        return status;
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

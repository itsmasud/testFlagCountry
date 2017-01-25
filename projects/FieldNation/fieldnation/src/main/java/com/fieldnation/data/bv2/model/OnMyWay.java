package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class OnMyWay {
    private static final String TAG = "OnMyWay";

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "substatus")
    private String _substatus;

    @Json(name = "created")
    private Date _created;

    @Json(name = "estimated_delay")
    private Integer _estimatedDelay;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "drive_time")
    private Integer _driveTime;

    @Json(name = "coords")
    private Coords _coords;

    @Json(name = "status")
    private String _status;

    public OnMyWay() {
    }

    public Double getDistance() {
        return _distance;
    }

    public String getSubstatus() {
        return _substatus;
    }

    public Date getCreated() {
        return _created;
    }

    public Integer getEstimatedDelay() {
        return _estimatedDelay;
    }

    public Boolean getActive() {
        return _active;
    }

    public Integer getDriveTime() {
        return _driveTime;
    }

    public Coords getCoords() {
        return _coords;
    }

    public String getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static OnMyWay fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(OnMyWay.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}

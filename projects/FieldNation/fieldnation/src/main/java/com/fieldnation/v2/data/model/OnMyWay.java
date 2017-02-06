package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class OnMyWay implements Parcelable {
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

    public void setDistance(Double distance) {
        _distance = distance;
    }

    public Double getDistance() {
        return _distance;
    }

    public OnMyWay distance(Double distance) {
        _distance = distance;
        return this;
    }

    public void setSubstatus(String substatus) {
        _substatus = substatus;
    }

    public String getSubstatus() {
        return _substatus;
    }

    public OnMyWay substatus(String substatus) {
        _substatus = substatus;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public OnMyWay created(Date created) {
        _created = created;
        return this;
    }

    public void setEstimatedDelay(Integer estimatedDelay) {
        _estimatedDelay = estimatedDelay;
    }

    public Integer getEstimatedDelay() {
        return _estimatedDelay;
    }

    public OnMyWay estimatedDelay(Integer estimatedDelay) {
        _estimatedDelay = estimatedDelay;
        return this;
    }

    public void setActive(Boolean active) {
        _active = active;
    }

    public Boolean getActive() {
        return _active;
    }

    public OnMyWay active(Boolean active) {
        _active = active;
        return this;
    }

    public void setDriveTime(Integer driveTime) {
        _driveTime = driveTime;
    }

    public Integer getDriveTime() {
        return _driveTime;
    }

    public OnMyWay driveTime(Integer driveTime) {
        _driveTime = driveTime;
        return this;
    }

    public void setCoords(Coords coords) {
        _coords = coords;
    }

    public Coords getCoords() {
        return _coords;
    }

    public OnMyWay coords(Coords coords) {
        _coords = coords;
        return this;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public String getStatus() {
        return _status;
    }

    public OnMyWay status(String status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static OnMyWay[] fromJsonArray(JsonArray array) {
        OnMyWay[] list = new OnMyWay[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<OnMyWay> CREATOR = new Parcelable.Creator<OnMyWay>() {

        @Override
        public OnMyWay createFromParcel(Parcel source) {
            try {
                return OnMyWay.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public OnMyWay[] newArray(int size) {
            return new OnMyWay[size];
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

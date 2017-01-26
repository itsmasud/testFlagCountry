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

public class CheckInOut implements Parcelable {
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

    public void setTimeLog(CheckInOutTimeLog timeLog) {
        _timeLog = timeLog;
    }

    public CheckInOutTimeLog getTimeLog() {
        return _timeLog;
    }

    public CheckInOut timeLog(CheckInOutTimeLog timeLog) {
        _timeLog = timeLog;
        return this;
    }

    public void setActor(User actor) {
        _actor = actor;
    }

    public User getActor() {
        return _actor;
    }

    public CheckInOut actor(User actor) {
        _actor = actor;
        return this;
    }

    public void setDistance(Double distance) {
        _distance = distance;
    }

    public Double getDistance() {
        return _distance;
    }

    public CheckInOut distance(Double distance) {
        _distance = distance;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public CheckInOut created(Date created) {
        _created = created;
        return this;
    }

    public void setVerified(Boolean verified) {
        _verified = verified;
    }

    public Boolean getVerified() {
        return _verified;
    }

    public CheckInOut verified(Boolean verified) {
        _verified = verified;
        return this;
    }

    public void setDistanceFromCheckIn(Double distanceFromCheckIn) {
        _distanceFromCheckIn = distanceFromCheckIn;
    }

    public Double getDistanceFromCheckIn() {
        return _distanceFromCheckIn;
    }

    public CheckInOut distanceFromCheckIn(Double distanceFromCheckIn) {
        _distanceFromCheckIn = distanceFromCheckIn;
        return this;
    }

    public void setCoords(Coords coords) {
        _coords = coords;
    }

    public Coords getCoords() {
        return _coords;
    }

    public CheckInOut coords(Coords coords) {
        _coords = coords;
        return this;
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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CheckInOut> CREATOR = new Parcelable.Creator<CheckInOut>() {

        @Override
        public CheckInOut createFromParcel(Parcel source) {
            try {
                return CheckInOut.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CheckInOut[] newArray(int size) {
            return new CheckInOut[size];
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

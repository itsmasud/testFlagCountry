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

public class CheckInOut implements Parcelable {
    private static final String TAG = "CheckInOut";

    @Json(name = "actor")
    private User _actor;

    @Json(name = "coords")
    private Coords _coords;

    @Json(name = "created")
    private Date _created;

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "distance_from_check_in")
    private Double _distanceFromCheckIn;

    @Json(name = "time_log")
    private CheckInOutTimeLog _timeLog;

    @Json(name = "verified")
    private Boolean _verified;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public CheckInOut() {
    }

    public void setActor(User actor) throws ParseException {
        _actor = actor;
        SOURCE.put("actor", actor.getJson());
    }

    public User getActor() {
        return _actor;
    }

    public CheckInOut actor(User actor) throws ParseException {
        _actor = actor;
        SOURCE.put("actor", actor.getJson());
        return this;
    }

    public void setCoords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
    }

    public Coords getCoords() {
        return _coords;
    }

    public CheckInOut coords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public CheckInOut created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setDistance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
    }

    public Double getDistance() {
        return _distance;
    }

    public CheckInOut distance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
        return this;
    }

    public void setDistanceFromCheckIn(Double distanceFromCheckIn) throws ParseException {
        _distanceFromCheckIn = distanceFromCheckIn;
        SOURCE.put("distance_from_check_in", distanceFromCheckIn);
    }

    public Double getDistanceFromCheckIn() {
        return _distanceFromCheckIn;
    }

    public CheckInOut distanceFromCheckIn(Double distanceFromCheckIn) throws ParseException {
        _distanceFromCheckIn = distanceFromCheckIn;
        SOURCE.put("distance_from_check_in", distanceFromCheckIn);
        return this;
    }

    public void setTimeLog(CheckInOutTimeLog timeLog) throws ParseException {
        _timeLog = timeLog;
        SOURCE.put("time_log", timeLog.getJson());
    }

    public CheckInOutTimeLog getTimeLog() {
        return _timeLog;
    }

    public CheckInOut timeLog(CheckInOutTimeLog timeLog) throws ParseException {
        _timeLog = timeLog;
        SOURCE.put("time_log", timeLog.getJson());
        return this;
    }

    public void setVerified(Boolean verified) throws ParseException {
        _verified = verified;
        SOURCE.put("verified", verified);
    }

    public Boolean getVerified() {
        return _verified;
    }

    public CheckInOut verified(Boolean verified) throws ParseException {
        _verified = verified;
        SOURCE.put("verified", verified);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CheckInOut[] array) {
        JsonArray list = new JsonArray();
        for (CheckInOut item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CheckInOut[] fromJsonArray(JsonArray array) {
        CheckInOut[] list = new CheckInOut[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CheckInOut fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CheckInOut.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
